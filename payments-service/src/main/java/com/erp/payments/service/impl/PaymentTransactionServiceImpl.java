package com.erp.payments.service.impl;

import com.erp.common.dto.payments.PaymentMethodStatsDto;
import com.erp.common.dto.payments.PaymentTransactionDto;
import com.erp.common.dto.payments.PaymentTrendDto;
import com.erp.common.enums.PaymentTxMethod;
import com.erp.common.enums.PaymentTxStatus;
import com.erp.common.entity.PaymentTransaction;
import com.erp.payments.exception.ResourceNotFoundException;
import com.erp.payments.mapper.PaymentTransactionMapper;
import com.erp.payments.repository.PaymentTransactionRepository;
import com.erp.payments.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
@Transactional(readOnly = true)
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final PaymentTransactionRepository repo;
    private final PaymentTransactionMapper mapper;

    @Override
    public Page<PaymentTransactionDto> search(PaymentTxStatus status, PaymentTxMethod method, String q, Pageable pageable) {
        String query = (q != null && !q.isBlank()) ? q : null;
        return repo.search(status, method, query, pageable).map(mapper::toDto);
    }

    @Override
    public PaymentTransactionDto findById(Long id) { return mapper.toDto(get(id)); }

    @Override
    public PaymentTransactionDto findByCode(String code) {
        return mapper.toDto(repo.findByPaymentCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentTransaction", "code", code)));
    }

    @Override @Transactional
    public PaymentTransactionDto create(PaymentTransactionDto dto) {
        PaymentTransaction tx = mapper.toEntity(dto);
        // Auto-generate reference if not provided
        if (tx.getReference() == null || tx.getReference().isBlank()) {
            tx.setReference(generateReference(tx.getMethod()));
        }
        PaymentTransaction saved = repo.save(tx);
        log.info("Recorded payment [{}] {} {} {}", saved.getPaymentCode(), saved.getAmount(), saved.getMethod(), saved.getStatus());
        return mapper.toDto(saved);
    }

    @Override @Transactional
    public PaymentTransactionDto updateStatus(Long id, PaymentTxStatus status) {
        PaymentTransaction tx = get(id);
        if (tx.getStatus() == PaymentTxStatus.REFUNDED)
            throw new IllegalArgumentException("Cannot change status of a refunded payment");
        tx.setStatus(status);
        return mapper.toDto(repo.save(tx));
    }

    @Override @Transactional
    public PaymentTransactionDto refund(Long id) {
        PaymentTransaction original = get(id);
        if (original.getStatus() == PaymentTxStatus.REFUNDED)
            throw new IllegalArgumentException("Payment " + original.getPaymentCode() + " is already refunded");
        if (original.getStatus() != PaymentTxStatus.COMPLETED)
            throw new IllegalArgumentException("Only COMPLETED payments can be refunded");

        original.setStatus(PaymentTxStatus.REFUNDED);
        repo.save(original);

        String refCode = original.getPaymentCode() + "-REF";
        PaymentTransaction refund = PaymentTransaction.builder()
                .paymentCode(refCode)
                .date(LocalDate.now())
                .payer(original.getPayer())
                .description("Refund for " + original.getPaymentCode() + " — " + original.getDescription())
                .amount(original.getAmount())
                .method(original.getMethod())
                .status(PaymentTxStatus.REFUNDED)
                .reference(generateReference(original.getMethod()))
                .build();
        return mapper.toDto(repo.save(refund));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("PaymentTransaction", "id", id);
        repo.deleteById(id);
    }

    @Override
    public List<PaymentMethodStatsDto> getStatsByMethod() {
        return repo.aggregateByMethod().stream()
                .map(row -> PaymentMethodStatsDto.builder()
                        .method((PaymentTxMethod) row[0])
                        .total((BigDecimal) row[1])
                        .count((long) row[2])
                        .build())
                .toList();
    }

    @Override
    public List<PaymentTrendDto> getWeeklyTrend(int weeks) {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusWeeks(weeks).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<PaymentTransaction> txs = repo.findByDateBetweenOrderByDateAsc(from, today);

        // Build week buckets
        List<PaymentTrendDto> result = new ArrayList<>();
        for (int i = weeks - 1; i >= 0; i--) {
            LocalDate weekStart = today.minusWeeks(i).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate weekEnd   = weekStart.plusDays(6);

            // Label: e.g. "W1 Mar"
            int weekOfMonth = ((weekStart.getDayOfMonth() - 1) / 7) + 1;
            String label = "W" + weekOfMonth + " " + weekStart.getMonth().getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH);

            BigDecimal received    = BigDecimal.ZERO;
            BigDecimal outstanding = BigDecimal.ZERO;

            for (PaymentTransaction tx : txs) {
                if (!tx.getDate().isBefore(weekStart) && !tx.getDate().isAfter(weekEnd)) {
                    if (tx.getStatus() == PaymentTxStatus.COMPLETED) received = received.add(tx.getAmount());
                    else if (tx.getStatus() == PaymentTxStatus.PENDING) outstanding = outstanding.add(tx.getAmount());
                }
            }
            result.add(PaymentTrendDto.builder().week(label).received(received).outstanding(outstanding).build());
        }
        return result;
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private PaymentTransaction get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("PaymentTransaction", "id", id));
    }

    private String generateReference(PaymentTxMethod method) {
        String prefix = switch (method) {
            case ACH_TRANSFER   -> "ACH";
            case WIRE_TRANSFER  -> "WIRE";
            case CREDIT_CARD    -> "CC";
            case CHECK          -> "CHK";
            case DIGITAL_WALLET -> "DW";
        };
        return prefix + "-" + (10000 + new Random().nextInt(90000));
    }
}

