package com.erp.finance.service.impl;

import com.erp.common.dto.finance.BillDto;
import com.erp.common.dto.finance.InvoiceLineDto;
import com.erp.common.enums.*;
import com.erp.common.entity.Bill;
import com.erp.common.entity.BillLine;
import com.erp.common.entity.OutgoingPayment;
import com.erp.finance.exception.ResourceNotFoundException;
import com.erp.finance.mapper.BillMapper;
import com.erp.finance.repository.BillRepository;
import com.erp.finance.repository.OutgoingPaymentRepository;
import com.erp.finance.service.BillService;
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
public class BillServiceImpl implements BillService {

    private static final Map<InvoiceDocStatus, InvoiceDocStatus> STATUS_FLOW = Map.of(
            InvoiceDocStatus.DRAFT, InvoiceDocStatus.TO_PAY,
            InvoiceDocStatus.TO_PAY, InvoiceDocStatus.DONE
    );

    private final BillRepository billRepo;
    private final OutgoingPaymentRepository paymentRepo;
    private final BillMapper mapper;

    @Override
    public Page<BillDto> search(InvoiceDocStatus status, String q, Pageable pageable) {
        return billRepo.search(status, q, pageable).map(mapper::toDto);
    }

    @Override public BillDto findById(Long id) { return mapper.toDto(getOrThrow(id)); }

    @Override
    public BillDto findByNumber(String number) {
        return mapper.toDto(billRepo.findByBillNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "number", number)));
    }

    @Override @Transactional
    public BillDto create(BillDto dto) {
        Bill bill = mapper.toEntity(dto);
        syncLines(bill, dto.getLines());
        recalcTotals(bill);
        return mapper.toDto(billRepo.save(bill));
    }

    @Override @Transactional
    public BillDto update(Long id, BillDto dto) {
        Bill bill = getOrThrow(id);
        mapper.updateFromDto(dto, bill);
        syncLines(bill, dto.getLines());
        recalcTotals(bill);
        return mapper.toDto(billRepo.save(bill));
    }

    @Override @Transactional
    public BillDto advanceStatus(Long id) {
        Bill bill = getOrThrow(id);
        InvoiceDocStatus next = STATUS_FLOW.get(bill.getStatus());
        if (next == null) throw new IllegalArgumentException("Cannot advance status from " + bill.getStatus());
        bill.setStatus(next);
        return mapper.toDto(billRepo.save(bill));
    }

    @Override @Transactional
    public BillDto cancel(Long id) {
        Bill bill = getOrThrow(id);
        bill.setStatus(InvoiceDocStatus.CANCELED);
        return mapper.toDto(billRepo.save(bill));
    }

    @Override @Transactional
    public BillDto registerPayment(Long id) {
        Bill bill = getOrThrow(id);
        if (bill.getDebt().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("No outstanding debt for bill " + bill.getBillNumber());

        String pmtNumber = "PMT-OUT-" + String.format("%03d", paymentRepo.count() + 1);
        OutgoingPayment payment = OutgoingPayment.builder()
                .paymentNumber(pmtNumber)
                .type(FinancePaymentType.BANK)
                .date(LocalDate.now())
                .amount(bill.getDebt())
                .partner(bill.getVendor())
                .company("NexaERP Corp")
                .companyAccount("NEXA-MAIN-USD")
                .note("Payment for " + bill.getBillNumber())
                .base(bill.getBillNumber())
                .status(OutgoingPaymentStatus.DONE)
                .matchStatus(PaymentMatchStatus.MATCHED)
                .matchedAmount(bill.getDebt())
                .matchedDocs(new ArrayList<>(List.of(bill.getBillNumber())))
                .build();
        paymentRepo.save(payment);

        bill.setPaid(bill.getAmount());
        bill.setDebt(BigDecimal.ZERO);
        return mapper.toDto(billRepo.save(bill));
    }

    @Override @Transactional
    public BillDto createCorrection(Long id, CorrectionMode mode) {
        Bill original = getOrThrow(id);
        long count = billRepo.findByOriginalBillNumber(original.getBillNumber()).size();
        String corrNumber = original.getBillNumber() + "-C" + (count + 1);

        Bill corr = Bill.builder()
                .billNumber(corrNumber)
                .type(original.getType())
                .vendor(original.getVendor())
                .contract(original.getContract())
                .date(LocalDate.now())
                .dueDate(original.getDueDate())
                .paymentTerms(original.getPaymentTerms())
                .status(InvoiceDocStatus.DRAFT)
                .subtotal(original.getSubtotal())
                .taxTotal(original.getTaxTotal())
                .amount(original.getAmount())
                .paid(BigDecimal.ZERO)
                .debt(original.getAmount())
                .note("Correction of " + original.getBillNumber())
                .originalBillNumber(original.getBillNumber())
                .correctionMode(mode)
                .build();

        original.getLines().forEach(l -> {
            BillLine cl = new BillLine();
            cl.setLineRef(l.getLineRef());
            cl.setItem(l.getItem());
            cl.setDescription(l.getDescription());
            cl.setQty(l.getQty());
            cl.setPrice(l.getPrice());
            cl.setTaxRate(l.getTaxRate());
            cl.setSubtotal(l.getSubtotal());
            cl.setTaxAmount(l.getTaxAmount());
            cl.setTotal(l.getTotal());
            cl.setBill(corr);
            corr.getLines().add(cl);
        });

        return mapper.toDto(billRepo.save(corr));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!billRepo.existsById(id)) throw new ResourceNotFoundException("Bill", "id", id);
        billRepo.deleteById(id);
    }

    private Bill getOrThrow(Long id) {
        return billRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));
    }

    private void syncLines(Bill bill, List<InvoiceLineDto> dtos) {
        bill.getLines().clear();
        if (dtos == null) return;
        int i = 1;
        for (InvoiceLineDto dto : dtos) {
            BillLine line = new BillLine();
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
            line.setBill(bill);
            bill.getLines().add(line);
        }
    }

    private void recalcTotals(Bill bill) {
        BigDecimal sub = bill.getLines().stream().map(BillLine::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = bill.getLines().stream().map(BillLine::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        bill.setSubtotal(sub);
        bill.setTaxTotal(tax);
        bill.setAmount(sub.add(tax));
        bill.setDebt(bill.getAmount().subtract(bill.getPaid()).max(BigDecimal.ZERO));
    }
}

