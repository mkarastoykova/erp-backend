package com.erp.finance.service.impl;

import com.erp.common.dto.finance.IncomingPaymentDto;
import com.erp.common.enums.PaymentMatchStatus;
import com.erp.finance.exception.ResourceNotFoundException;
import com.erp.finance.mapper.IncomingPaymentMapper;
import com.erp.finance.repository.IncomingPaymentRepository;
import com.erp.finance.service.IncomingPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service @RequiredArgsConstructor @Slf4j
@Transactional(readOnly = true)
public class IncomingPaymentServiceImpl implements IncomingPaymentService {

    private final IncomingPaymentRepository repo;
    private final IncomingPaymentMapper mapper;

    @Override
    public Page<IncomingPaymentDto> search(PaymentMatchStatus matchStatus, String q, Pageable pageable) {
        return repo.search(matchStatus, q, pageable).map(mapper::toDto);
    }

    @Override
    public IncomingPaymentDto findById(Long id) {
        return mapper.toDto(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncomingPayment", "id", id)));
    }

    @Override @Transactional
    public IncomingPaymentDto create(IncomingPaymentDto dto) {
        return mapper.toDto(repo.save(mapper.toEntity(dto)));
    }

    @Override @Transactional
    public IncomingPaymentDto update(Long id, IncomingPaymentDto dto) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncomingPayment", "id", id));
        mapper.updateFromDto(dto, entity);
        return mapper.toDto(repo.save(entity));
    }

    @Override @Transactional
    public IncomingPaymentDto matchDocument(Long id, String docNumber, BigDecimal amount) {
        var payment = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncomingPayment", "id", id));
        BigDecimal newMatched = payment.getMatchedAmount().add(amount)
                .min(payment.getAmount());
        payment.setMatchedAmount(newMatched);
        if (!payment.getMatchedDocs().contains(docNumber)) {
            payment.getMatchedDocs().add(docNumber);
        }
        payment.setMatchStatus(newMatched.compareTo(payment.getAmount()) >= 0
                ? PaymentMatchStatus.MATCHED : PaymentMatchStatus.PARTIAL);
        return mapper.toDto(repo.save(payment));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("IncomingPayment", "id", id);
        repo.deleteById(id);
    }
}

