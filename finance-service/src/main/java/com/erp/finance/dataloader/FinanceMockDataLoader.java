package com.erp.finance.dataloader;

import com.erp.common.enums.*;
import com.erp.common.entity.*;
import com.erp.finance.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the frontend mock data into the database.
 * Only runs when the "mock" Spring profile is active.
 * Start the service with: --spring.profiles.active=mock
 */
@Component
@Profile("mock")
@RequiredArgsConstructor
@Slf4j
public class FinanceMockDataLoader implements ApplicationRunner {

    private final TaxDefinitionRepository taxRepo;
    private final PaymentTermRepository termRepo;
    private final InvoiceRepository invoiceRepo;
    private final BillRepository billRepo;
    private final IncomingPaymentRepository inPayRepo;
    private final OutgoingPaymentRepository outPayRepo;
    private final JournalEntryRepository journalRepo;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (taxRepo.count() > 0) {
            log.info("Mock data already loaded — skipping.");
            return;
        }
        log.info("Loading Finance mock data…");
        loadTaxDefinitions();
        loadPaymentTerms();
        loadInvoices();
        loadBills();
        loadIncomingPayments();
        loadOutgoingPayments();
        loadJournalEntries();
        log.info("Finance mock data loaded successfully.");
    }

    // ── Tax definitions ──────────────────────────────────────────────────────

    private void loadTaxDefinitions() {
        taxRepo.saveAll(List.of(
            tax("TX-S1", "Standard 10%", 10.0, FinanceTaxType.BOTH,     true),
            tax("TX-S2", "Reduced 5%",   5.0,  FinanceTaxType.BOTH,     true),
            tax("TX-S3", "High 20%",     20.0, FinanceTaxType.SALES,    true),
            tax("TX-S4", "Low 15%",      15.0, FinanceTaxType.PURCHASE, true),
            tax("TX-S5", "Zero-rated",   0.0,  FinanceTaxType.BOTH,     true)
        ));
    }

    private TaxDefinition tax(String code, String name, double rate, FinanceTaxType type, boolean active) {
        return TaxDefinition.builder().taxCode(code).name(name).rate(rate).taxType(type).active(active).build();
    }

    // ── Payment terms ────────────────────────────────────────────────────────

    private void loadPaymentTerms() {
        termRepo.saveAll(List.of(
            term("PT1", "Immediate", 0,  "Payment due upon receipt"),
            term("PT2", "Net 15",    15, "Payment due in 15 days"),
            term("PT3", "Net 30",    30, "Payment due in 30 days"),
            term("PT4", "Net 60",    60, "Payment due in 60 days"),
            term("PT5", "Net 90",    90, "Payment due in 90 days")
        ));
    }

    private PaymentTermDef term(String code, String name, int days, String desc) {
        return PaymentTermDef.builder().termCode(code).name(name).dueDays(days).description(desc).build();
    }

    // ── Invoices ─────────────────────────────────────────────────────────────

    private void loadInvoices() {
        invoiceRepo.saveAll(List.of(
            inv("INV-1001","Acme Corp",            "CON-2024-001","2026-03-01","2026-03-31",FinancePaymentTerms.NET_30,InvoiceDocStatus.DONE,    8485.00,  "Q1 delivery",          false,
                line("Laptop Pro 15\"",          "15-inch business laptop",  5, 1299.00, InvoiceTaxRate.TEN),
                line("Enterprise SW License",    "Annual per-seat license", 10,  199.00, InvoiceTaxRate.NONE)),
            inv("INV-1002","TechNova Ltd",          "CON-2024-002","2026-03-05","2026-04-04",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,  0.0,      "Annual renewal",        false,
                line("IT Support Package",       "Annual support contract",  3, 2999.00, InvoiceTaxRate.NONE),
                line("Cloud Storage 1TB",        "12-month subscription",   5,  240.00, InvoiceTaxRate.NONE)),
            inv("INV-1003","GlobalRetail Inc",      "CON-2025-003","2026-02-15","2026-03-15",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,  0.0,      "Overdue — follow up",  false,
                line("Monitor 27\" 4K",          "4K UHD display",          10,  549.00, InvoiceTaxRate.TEN),
                line("USB-C Hub 7-in-1",         "7-port USB-C hub",        10,   59.00, InvoiceTaxRate.TEN)),
            inv("INV-1004","BlueSky Partners",      "",            "2026-03-10","2026-04-09",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,  0.0,      "",                      false,
                line("Ergonomic Chair",          "Fully adjustable chair",   5,  449.00, InvoiceTaxRate.TEN)),
            inv("INV-1005","Sunrise Manufacturing", "CON-2024-005","2026-03-12","2026-04-11",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,  0.0,      "New office fit-out",    false,
                line("Standing Desk 60\"",       "Electric adjustable desk", 8,  799.00, InvoiceTaxRate.TEN),
                line("Ergonomic Chair",          "Office chair",             8,  449.00, InvoiceTaxRate.TEN)),
            inv("INV-1006","Vertex Solutions",      "",            "2026-02-20","2026-03-20",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,  0.0,      "Overdue",               false,
                line("IT Support Package",       "Support contract",         1, 2999.00, InvoiceTaxRate.NONE)),
            inv("INV-1007","PinnacleTech",          "CON-2025-007","2026-03-15","2026-04-14",FinancePaymentTerms.NET_30,InvoiceDocStatus.DRAFT,   0.0,      "Pending review",        false,
                line("Analytics Platform",       "Business analytics",       1, 9500.00, InvoiceTaxRate.TEN),
                line("Setup Fee",                "One-time setup",           1,  800.00, InvoiceTaxRate.TEN)),
            inv("INV-1008","Atlantic Logistics",    "CON-2024-008","2026-03-18","2026-04-17",FinancePaymentTerms.NET_30,InvoiceDocStatus.DONE,    18900.0,  "Paid in full",          false,
                line("Supply Chain Suite",       "12-month license",         1,14400.00, InvoiceTaxRate.NONE),
                line("Training Package",         "On-site training days",    3, 1500.00, InvoiceTaxRate.TEN)),
            inv("INV-1009","Nexus Dynamics",        "CON-2024-007","2026-03-20","2026-04-19",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,  0.0,      "",                      false,
                line("Dev Tools License",        "Annual dev license",       5,  780.00, InvoiceTaxRate.NONE)),
            inv("INV-1010","Orion Enterprises",     "CON-2025-008","2026-03-22","2026-04-21",FinancePaymentTerms.NET_60,InvoiceDocStatus.DRAFT,   0.0,      "",                      false,
                line("Logistics Suite",          "ERP logistics module",     1,18500.00, InvoiceTaxRate.NONE),
                line("Implementation",           "Setup & migration",        1, 4500.00, InvoiceTaxRate.NONE))
        ));
    }

    private Invoice inv(String number, String customer, String contract,
                        String date, String dueDate,
                        FinancePaymentTerms terms, InvoiceDocStatus status,
                        double paid, String note, boolean shipment,
                        InvoiceLine... lineArr) {
        Invoice inv = Invoice.builder()
                .invoiceNumber(number)
                .type(InvoiceType.CUSTOMER_INVOICE)
                .customer(customer)
                .contract(contract)
                .date(LocalDate.parse(date))
                .dueDate(LocalDate.parse(dueDate))
                .paymentTerms(terms)
                .status(status)
                .paid(BigDecimal.valueOf(paid))
                .note(note)
                .shipmentCreated(shipment)
                .build();
        for (InvoiceLine l : lineArr) { l.setInvoice(inv); inv.getLines().add(l); }
        recalc(inv);
        return inv;
    }

    private InvoiceLine line(String item, String desc, int qty, double price, InvoiceTaxRate tax) {
        InvoiceLine l = new InvoiceLine();
        l.setItem(item); l.setDescription(desc); l.setQty(qty);
        l.setPrice(BigDecimal.valueOf(price)); l.setTaxRate(tax);
        BigDecimal sub = BigDecimal.valueOf(price * qty);
        BigDecimal taxAmt = sub.multiply(BigDecimal.valueOf(tax.getPercent()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        l.setSubtotal(sub); l.setTaxAmount(taxAmt); l.setTotal(sub.add(taxAmt));
        return l;
    }

    private void recalc(Invoice inv) {
        BigDecimal sub = inv.getLines().stream().map(InvoiceLine::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = inv.getLines().stream().map(InvoiceLine::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        inv.setSubtotal(sub); inv.setTaxTotal(tax); inv.setAmount(sub.add(tax));
        inv.setDebt(inv.getAmount().subtract(inv.getPaid()).max(BigDecimal.ZERO));
    }

    // ── Bills ─────────────────────────────────────────────────────────────────

    private void loadBills() {
        billRepo.saveAll(List.of(
            bill("BILL-001","Industrial Supplies Co","SUP-2026-001","2026-03-01","2026-03-31",FinancePaymentTerms.NET_30,InvoiceDocStatus.DONE,  4290.0, "March supply",               null, null,
                bline("Industrial Pump A200", "Pump unit",            2, 1200.00, InvoiceTaxRate.TEN),
                bline("Steel Pipe 2inch",     "10m sections",        20,   85.00, InvoiceTaxRate.TEN)),
            bill("BILL-002","TechParts Global",       "SUP-2026-002","2026-03-05","2026-04-04",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,0.0,    "",                            null, null,
                bline("Circuit Board CB-7",  "Electronics board",   10,  450.00, InvoiceTaxRate.TEN),
                bline("PLC Controller S7",   "Automation ctrl",      2, 3800.00, InvoiceTaxRate.TEN)),
            bill("BILL-003","SafeGear Solutions",      "",            "2026-03-08","2026-04-07",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,0.0,    "",                            null, null,
                bline("Safety Helmet XL",    "Hard hat PPE",        30,   38.00, InvoiceTaxRate.TEN),
                bline("Work Gloves pair",    "Safety gloves",       50,    6.00, InvoiceTaxRate.TEN)),
            bill("BILL-004","MetalWorks Ltd",          "SUP-2025-004","2026-03-10","2026-04-09",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,0.0,    "Rush order",                  null, null,
                bline("Steel Components",    "Custom fabricated", 100,  120.00, InvoiceTaxRate.TEN)),
            bill("BILL-005","ElectroPro Inc",           "",            "2026-03-12","2026-04-11",FinancePaymentTerms.NET_30,InvoiceDocStatus.DRAFT, 0.0,    "Pending receipt",             null, null,
                bline("Electric Motor 5HP",  "Heavy motor unit",    5, 2200.00, InvoiceTaxRate.TEN)),
            bill("BILL-006","ConsumPro Dist",           "SUP-2026-006","2026-02-25","2026-03-26",FinancePaymentTerms.NET_30,InvoiceDocStatus.DONE,  1250.0, "Monthly consumables",         null, null,
                bline("Lubricant Oil 5L",    "Industrial lubricant",50,   25.00, InvoiceTaxRate.NONE)),
            bill("BILL-007","HeavyMach Corp",           "SUP-2025-007","2026-03-15","2026-04-14",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,0.0,    "Major equipment purchase",    null, null,
                bline("CNC Milling Machine", "Industrial CNC",       1,86000.00, InvoiceTaxRate.TEN)),
            bill("BILL-008","Office Supplies Ltd",      "",            "2026-03-18","2026-04-17",FinancePaymentTerms.NET_15,InvoiceDocStatus.DONE,  480.0,  "",                            null, null,
                bline("Office Stationery",   "Monthly supplies",     1,  480.00, InvoiceTaxRate.NONE)),
            // Correction
            bill("BILL-002-C1","TechParts Global",     "SUP-2026-002","2026-03-15","2026-04-04",FinancePaymentTerms.NET_30,InvoiceDocStatus.TO_PAY,0.0, "Price correction per supplier credit note","BILL-002", CorrectionMode.REPLACEMENT,
                bline("Circuit Board CB-7",  "Correction — price adjustment", 10, 430.00, InvoiceTaxRate.TEN))
        ));
    }

    private Bill bill(String number, String vendor, String contract,
                      String date, String dueDate,
                      FinancePaymentTerms terms, InvoiceDocStatus status,
                      double paid, String note,
                      String originalBillNumber, CorrectionMode corrMode,
                      BillLine... lineArr) {
        Bill b = Bill.builder()
                .billNumber(number).type(BillType.SUPPLIER_BILL)
                .vendor(vendor).contract(contract)
                .date(LocalDate.parse(date)).dueDate(LocalDate.parse(dueDate))
                .paymentTerms(terms).status(status)
                .paid(BigDecimal.valueOf(paid)).note(note)
                .originalBillNumber(originalBillNumber).correctionMode(corrMode)
                .build();
        for (BillLine l : lineArr) { l.setBill(b); b.getLines().add(l); }
        recalcBill(b);
        return b;
    }

    private BillLine bline(String item, String desc, int qty, double price, InvoiceTaxRate tax) {
        BillLine l = new BillLine();
        l.setItem(item); l.setDescription(desc); l.setQty(qty);
        l.setPrice(BigDecimal.valueOf(price)); l.setTaxRate(tax);
        BigDecimal sub = BigDecimal.valueOf(price * qty);
        BigDecimal taxAmt = sub.multiply(BigDecimal.valueOf(tax.getPercent()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        l.setSubtotal(sub); l.setTaxAmount(taxAmt); l.setTotal(sub.add(taxAmt));
        return l;
    }

    private void recalcBill(Bill b) {
        BigDecimal sub = b.getLines().stream().map(BillLine::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = b.getLines().stream().map(BillLine::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        b.setSubtotal(sub); b.setTaxTotal(tax); b.setAmount(sub.add(tax));
        b.setDebt(b.getAmount().subtract(b.getPaid()).max(BigDecimal.ZERO));
    }

    // ── Incoming payments ─────────────────────────────────────────────────────

    private void loadIncomingPayments() {
        inPayRepo.saveAll(List.of(
            inPay("IN-001","PMT-IN-001",FinancePaymentType.BANK,"2026-03-08",8485.00,  "Acme Corp",         "ACME-BANK-001","INV-1001 settlement",   "INV-1001",IncomingPaymentStatus.DONE,PaymentMatchStatus.MATCHED,  8485.00, "INV-1001"),
            inPay("IN-002","PMT-IN-002",FinancePaymentType.BANK,"2026-03-22",18900.00, "Atlantic Logistics", "ATL-BANK-001", "INV-1008 full payment", "INV-1008",IncomingPaymentStatus.DONE,PaymentMatchStatus.MATCHED, 18900.00, "INV-1008"),
            inPay("IN-003","PMT-IN-003",FinancePaymentType.CASH,"2026-03-18",5000.00,  "BlueSky Partners",   "",             "Partial advance",       "Advance",  IncomingPaymentStatus.DONE,PaymentMatchStatus.NOT_MATCHED,0.0),
            inPay("IN-004","PMT-IN-004",FinancePaymentType.BANK,"2026-03-25",3900.00,  "Nexus Dynamics",     "NEX-BANK-001", "Partial payment INV-1009","INV-1009",IncomingPaymentStatus.DONE,PaymentMatchStatus.PARTIAL, 3900.00, "INV-1009")
        ));
    }

    private IncomingPayment inPay(String id, String number, FinancePaymentType type, String date,
                                  double amount, String partner, String partnerAccount,
                                  String note, String base,
                                  IncomingPaymentStatus status, PaymentMatchStatus matchStatus,
                                  double matchedAmount, String... docs) {
        return IncomingPayment.builder()
                .paymentNumber(number).type(type)
                .date(LocalDate.parse(date))
                .amount(BigDecimal.valueOf(amount))
                .partner(partner).partnerAccount(partnerAccount)
                .company("NexaERP Corp").companyAccount("NEXA-MAIN-USD")
                .note(note).base(base)
                .status(status).matchStatus(matchStatus)
                .matchedAmount(BigDecimal.valueOf(matchedAmount))
                .matchedDocs(new ArrayList<>(List.of(docs)))
                .build();
    }

    // ── Outgoing payments ─────────────────────────────────────────────────────

    private void loadOutgoingPayments() {
        outPayRepo.saveAll(List.of(
            outPay("OUT-001","PMT-OUT-001",FinancePaymentType.BANK,"2026-03-12",4290.0,  "Industrial Supplies Co","INDSUP-BANK-01","BILL-001 settlement",    "BILL-001",OutgoingPaymentStatus.DONE,  PaymentMatchStatus.MATCHED, 4290.0,  "BILL-001"),
            outPay("OUT-002","PMT-OUT-002",FinancePaymentType.BANK,"2026-03-08",1250.0,  "ConsumPro Dist",         "CONPRO-BANK-01","BILL-006 consumables",   "BILL-006",OutgoingPaymentStatus.DONE,  PaymentMatchStatus.MATCHED, 1250.0,  "BILL-006"),
            outPay("OUT-003","PMT-OUT-003",FinancePaymentType.BANK,"2026-03-19",480.0,   "Office Supplies Ltd",    "",              "BILL-008 office supplies","BILL-008",OutgoingPaymentStatus.DONE, PaymentMatchStatus.MATCHED, 480.0,   "BILL-008"),
            outPay("OUT-004","PMT-OUT-004",FinancePaymentType.BANK,"2026-03-28",12000.0, "MetalWorks Ltd",         "METAL-BANK-01", "Partial advance BILL-004","BILL-004",OutgoingPaymentStatus.TO_PAY,PaymentMatchStatus.PARTIAL, 12000.0, "BILL-004")
        ));
    }

    private OutgoingPayment outPay(String id, String number, FinancePaymentType type, String date,
                                   double amount, String partner, String partnerAccount,
                                   String note, String base,
                                   OutgoingPaymentStatus status, PaymentMatchStatus matchStatus,
                                   double matchedAmount, String... docs) {
        return OutgoingPayment.builder()
                .paymentNumber(number).type(type)
                .date(LocalDate.parse(date))
                .amount(BigDecimal.valueOf(amount))
                .partner(partner).partnerAccount(partnerAccount)
                .company("NexaERP Corp").companyAccount("NEXA-MAIN-USD")
                .note(note).base(base)
                .status(status).matchStatus(matchStatus)
                .matchedAmount(BigDecimal.valueOf(matchedAmount))
                .matchedDocs(new ArrayList<>(List.of(docs)))
                .build();
    }

    // ── Journal entries ───────────────────────────────────────────────────────

    private void loadJournalEntries() {
        journalRepo.saveAll(List.of(
            je("JE-001","2026-03-01","Sales Revenue",         0,     45000, "Revenue"),
            je("JE-002","2026-03-01","Accounts Receivable",   45000, 0,     "Assets"),
            je("JE-003","2026-03-03","Office Rent",           8500,  0,     "Expenses"),
            je("JE-004","2026-03-03","Cash",                  0,     8500,  "Assets"),
            je("JE-005","2026-03-05","Payroll Expense",       62000, 0,     "Expenses"),
            je("JE-006","2026-03-05","Cash",                  0,     62000, "Assets"),
            je("JE-007","2026-03-08","Inventory Purchase",    18000, 0,     "Assets"),
            je("JE-008","2026-03-08","Accounts Payable",      0,     18000, "Liabilities")
        ));
    }

    private JournalEntry je(String number, String date, String desc, double debit, double credit, String account) {
        return JournalEntry.builder()
                .entryNumber(number).date(LocalDate.parse(date))
                .description(desc)
                .debit(BigDecimal.valueOf(debit))
                .credit(BigDecimal.valueOf(credit))
                .account(account).build();
    }
}

