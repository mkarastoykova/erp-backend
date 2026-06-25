package com.erp.finance.service.impl;

import com.erp.common.dto.finance.InvoiceDto;
import com.erp.common.dto.finance.InvoiceLineDto;
import com.erp.common.enums.IncomingPaymentStatus;
import com.erp.common.enums.InvoiceDocStatus;
import com.erp.common.enums.PaymentMatchStatus;
import com.erp.common.entity.Invoice;
import com.erp.common.entity.InvoiceLine;
import com.erp.common.entity.IncomingPayment;
import com.erp.finance.exception.ResourceNotFoundException;
import com.erp.finance.mapper.InvoiceMapper;
import com.erp.finance.repository.IncomingPaymentRepository;
import com.erp.finance.repository.InvoiceRepository;
import com.erp.finance.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor @Slf4j
@Transactional(readOnly = true)
public class InvoiceServiceImpl implements InvoiceService {

    private static final Map<InvoiceDocStatus, InvoiceDocStatus> STATUS_FLOW = Map.of(
            InvoiceDocStatus.DRAFT, InvoiceDocStatus.TO_PAY,
            InvoiceDocStatus.TO_PAY, InvoiceDocStatus.DONE
    );

    private final InvoiceRepository invoiceRepo;
    private final IncomingPaymentRepository paymentRepo;
    private final InvoiceMapper mapper;

    @Override
    public Page<InvoiceDto> search(InvoiceDocStatus status, String q, Pageable pageable) {
        return invoiceRepo.search(status, q, pageable).map(mapper::toDto);
    }

    @Override
    public InvoiceDto findById(Long id) {
        return mapper.toDto(getOrThrow(id));
    }

    @Override
    public InvoiceDto findByNumber(String number) {
        return mapper.toDto(invoiceRepo.findByInvoiceNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "number", number)));
    }

    @Override @Transactional
    public InvoiceDto create(InvoiceDto dto) {
        Invoice invoice = mapper.toEntity(dto);
        syncLines(invoice, dto.getLines());
        recalcTotals(invoice);
        Invoice saved = invoiceRepo.save(invoice);
        log.info("Created invoice {}", saved.getInvoiceNumber());
        return mapper.toDto(saved);
    }

    @Override @Transactional
    public InvoiceDto update(Long id, InvoiceDto dto) {
        Invoice invoice = getOrThrow(id);
        mapper.updateFromDto(dto, invoice);
        syncLines(invoice, dto.getLines());
        recalcTotals(invoice);
        return mapper.toDto(invoiceRepo.save(invoice));
    }

    @Override @Transactional
    public InvoiceDto advanceStatus(Long id) {
        Invoice invoice = getOrThrow(id);
        InvoiceDocStatus next = STATUS_FLOW.get(invoice.getStatus());
        if (next == null) throw new IllegalArgumentException("Cannot advance status from " + invoice.getStatus());
        invoice.setStatus(next);
        return mapper.toDto(invoiceRepo.save(invoice));
    }

    @Override @Transactional
    public InvoiceDto cancel(Long id) {
        Invoice invoice = getOrThrow(id);
        invoice.setStatus(InvoiceDocStatus.CANCELED);
        return mapper.toDto(invoiceRepo.save(invoice));
    }

    @Override @Transactional
    public InvoiceDto registerPayment(Long id) {
        Invoice invoice = getOrThrow(id);
        if (invoice.getDebt().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("No outstanding debt for invoice " + invoice.getInvoiceNumber());

        String pmtNumber = "PMT-IN-" + String.format("%03d", paymentRepo.count() + 1);
        IncomingPayment payment = IncomingPayment.builder()
                .paymentNumber(pmtNumber)
                .type(com.erp.common.enums.FinancePaymentType.BANK)
                .date(LocalDate.now())
                .amount(invoice.getDebt())
                .partner(invoice.getCustomer())
                .company("NexaERP Corp")
                .companyAccount("NEXA-MAIN-USD")
                .note("Payment for " + invoice.getInvoiceNumber())
                .base(invoice.getInvoiceNumber())
                .status(IncomingPaymentStatus.DONE)
                .matchStatus(PaymentMatchStatus.MATCHED)
                .matchedAmount(invoice.getDebt())
                .matchedDocs(new ArrayList<>(List.of(invoice.getInvoiceNumber())))
                .build();
        paymentRepo.save(payment);

        invoice.setPaid(invoice.getAmount());
        invoice.setDebt(BigDecimal.ZERO);
        return mapper.toDto(invoiceRepo.save(invoice));
    }

    @Override @Transactional
    public InvoiceDto markShipmentCreated(Long id) {
        Invoice invoice = getOrThrow(id);
        invoice.setShipmentCreated(true);
        return mapper.toDto(invoiceRepo.save(invoice));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!invoiceRepo.existsById(id)) throw new ResourceNotFoundException("Invoice", "id", id);
        invoiceRepo.deleteById(id);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Invoice getOrThrow(Long id) {
        return invoiceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));
    }

    private void syncLines(Invoice invoice, List<InvoiceLineDto> lineDtos) {
        invoice.getLines().clear();
        if (lineDtos == null) return;
        int i = 1;
        for (InvoiceLineDto dto : lineDtos) {
            InvoiceLine line = new InvoiceLine();
            line.setLineRef("L" + i++);
            line.setItem(dto.getItem());
            line.setDescription(dto.getDescription());
            line.setQty(dto.getQty());
            line.setPrice(dto.getPrice());
            line.setTaxRate(dto.getTaxRate());
            BigDecimal sub = dto.getPrice().multiply(BigDecimal.valueOf(dto.getQty()));
            double pct = dto.getTaxRate() != null ? dto.getTaxRate().getPercent() : 0.0;
            BigDecimal taxAmt = sub.multiply(BigDecimal.valueOf(pct))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            line.setSubtotal(sub);
            line.setTaxAmount(taxAmt);
            line.setTotal(sub.add(taxAmt));
            line.setInvoice(invoice);
            invoice.getLines().add(line);
        }
    }

    private void recalcTotals(Invoice invoice) {
        BigDecimal sub = invoice.getLines().stream().map(InvoiceLine::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = invoice.getLines().stream().map(InvoiceLine::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice.setSubtotal(sub);
        invoice.setTaxTotal(tax);
        invoice.setAmount(sub.add(tax));
        invoice.setDebt(invoice.getAmount().subtract(invoice.getPaid()).max(BigDecimal.ZERO));
    }
}

