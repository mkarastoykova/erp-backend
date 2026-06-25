package com.erp.payments.dataloader;

import com.erp.common.enums.PaymentTxMethod;
import com.erp.common.enums.PaymentTxStatus;
import com.erp.common.entity.PaymentTransaction;
import com.erp.payments.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Loads the 10 payment transactions from the frontend mock data.
 * Activate with: --spring.profiles.active=mock
 */
@Component
@Profile("mock")
@RequiredArgsConstructor
@Slf4j
public class PaymentMockDataLoader implements ApplicationRunner {

    private final PaymentTransactionRepository repo;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (repo.count() > 0) { log.info("Payment mock data already loaded — skipping."); return; }
        log.info("Loading Payment mock data…");

        repo.saveAll(List.of(
            tx("PAY-5001","2026-03-22","Acme Corp",            "INV-1001 Payment",        24500, PaymentTxMethod.ACH_TRANSFER,   PaymentTxStatus.COMPLETED, "ACH-88291"),
            tx("PAY-5002","2026-03-21","Atlantic Logistics",   "INV-1008 Payment",        28900, PaymentTxMethod.WIRE_TRANSFER,  PaymentTxStatus.COMPLETED, "WIRE-44512"),
            tx("PAY-5003","2026-03-20","Nexus Dynamics",       "INV-1009 Partial",         3000, PaymentTxMethod.CREDIT_CARD,    PaymentTxStatus.PENDING,   "CC-77342"),
            tx("PAY-5004","2026-03-19","TechNova Ltd",         "INV-1002 Payment",        18750, PaymentTxMethod.ACH_TRANSFER,   PaymentTxStatus.PENDING,   "ACH-88405"),
            tx("PAY-5005","2026-03-18","BlueSky Partners",     "Advance Payment",          5000, PaymentTxMethod.CHECK,          PaymentTxStatus.COMPLETED, "CHK-1120"),
            tx("PAY-5006","2026-03-17","Sunrise Manufacturing","Deposit INV-1005",        20000, PaymentTxMethod.WIRE_TRANSFER,  PaymentTxStatus.COMPLETED, "WIRE-44499"),
            tx("PAY-5007","2026-03-15","GlobalRetail Inc",     "Overdue INV-1003",        32000, PaymentTxMethod.ACH_TRANSFER,   PaymentTxStatus.FAILED,    "ACH-88310"),
            tx("PAY-5008","2026-03-14","PinnacleTech",         "Refund - returned goods",  4200, PaymentTxMethod.CREDIT_CARD,    PaymentTxStatus.REFUNDED,  "CC-77210"),
            tx("PAY-5009","2026-03-13","Orion Enterprises",    "Consulting Fees",         12000, PaymentTxMethod.DIGITAL_WALLET, PaymentTxStatus.COMPLETED, "DW-11098"),
            tx("PAY-5010","2026-03-12","Vertex Solutions",     "INV-1006 Payment",         7650, PaymentTxMethod.CHECK,          PaymentTxStatus.PENDING,   "CHK-1118")
        ));

        log.info("Payment mock data loaded: 10 transactions.");
    }

    private PaymentTransaction tx(String code, String date, String payer,
                                   String description, double amount,
                                   PaymentTxMethod method, PaymentTxStatus status,
                                   String reference) {
        return PaymentTransaction.builder()
                .paymentCode(code)
                .date(LocalDate.parse(date))
                .payer(payer)
                .description(description)
                .amount(BigDecimal.valueOf(amount))
                .method(method)
                .status(status)
                .reference(reference)
                .build();
    }
}

