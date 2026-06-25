package com.erp.crm.dataloader;

import com.erp.common.enums.*;
import com.erp.common.entity.*;
import com.erp.crm.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads all frontend CRM mock data into the database.
 * Activate with: --spring.profiles.active=mock
 */
@Component
@Profile("mock")
@RequiredArgsConstructor
@Slf4j
public class CrmMockDataLoader implements ApplicationRunner {

    private final CrmAccountRepository accountRepo;
    private final CrmContactRepository contactRepo;
    private final OpportunityRepository oppRepo;
    private final CrmActivityRepository activityRepo;
    private final SupportCaseRepository caseRepo;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (accountRepo.count() > 0) { log.info("CRM mock data already loaded — skipping."); return; }
        log.info("Loading CRM mock data…");
        loadAccounts();
        Map<String, CrmAccount> accMap = accountRepo.findAll().stream()
                .collect(Collectors.toMap(CrmAccount::getAccountCode, Function.identity()));
        loadContacts(accMap);
        Map<String, CrmContact> conMap = contactRepo.findAll().stream()
                .collect(Collectors.toMap(CrmContact::getContactCode, Function.identity()));
        loadOpportunities(accMap, conMap);
        loadActivities(conMap);
        loadCases(accMap);
        log.info("CRM mock data loaded successfully.");
    }

    // ── Accounts ─────────────────────────────────────────────────────────────

    private void loadAccounts() {
        accountRepo.saveAll(java.util.List.of(
            acc("ACC001","Acme Corp",            "Manufacturing",       AccountType.CUSTOMER,  520,  48000000.0, "Sarah Kim",   "+1-555-1001","acmecorp.com",    "USA",       CrmAccountStatus.ACTIVE,  "2024-03-15"),
            acc("ACC002","TechNova Ltd",          "Technology",          AccountType.CUSTOMER,  180,  22000000.0, "James Carter","+1-555-1002","technova.com",    "USA",       CrmAccountStatus.ACTIVE,  "2024-05-20"),
            acc("ACC003","GlobalRetail Inc",      "Retail",              AccountType.PROSPECT,  1200, 150000000.0,"Sarah Kim",   "+1-555-1003","globalretail.com","USA",       CrmAccountStatus.ACTIVE,  "2025-01-10"),
            acc("ACC004","BlueSky Partners",      "Financial Services",  AccountType.PROSPECT,  95,   18000000.0, "Mike Torres", "+1-555-1004","bluesky.com",     "Canada",    CrmAccountStatus.ACTIVE,  "2025-02-14"),
            acc("ACC005","Sunrise Manufacturing", "Manufacturing",       AccountType.CUSTOMER,  670,  85000000.0, "James Carter","+1-555-1005","sunrisemfg.com",  "USA",       CrmAccountStatus.ACTIVE,  "2024-11-05"),
            acc("ACC006","Vertex Solutions",      "Consulting",          AccountType.PARTNER,   45,   7500000.0,  "Mike Torres", "+1-555-1006","vertexsol.com",   "UK",        CrmAccountStatus.ACTIVE,  "2025-03-01"),
            acc("ACC007","Nexus Dynamics",        "Technology",          AccountType.CUSTOMER,  320,  55000000.0, "Sarah Kim",   "+1-555-1007","nexusdyn.com",    "USA",       CrmAccountStatus.ACTIVE,  "2024-07-18"),
            acc("ACC008","Orion Enterprises",     "Logistics",           AccountType.PROSPECT,  280,  35000000.0, "James Carter","+1-555-1008","orionent.com",    "Australia", CrmAccountStatus.INACTIVE,"2025-01-22"),
            acc("ACC009","PinnacleTech",          "Technology",          AccountType.PROSPECT,  150,  28000000.0, "Mike Torres", "+1-555-1009","pinnacletech.io",  "USA",       CrmAccountStatus.ACTIVE,  "2025-03-08"),
            acc("ACC010","Atlantic Logistics",    "Logistics",           AccountType.CUSTOMER,  440,  62000000.0, "Sarah Kim",   "+1-555-1010","atlanticlog.com",  "USA",       CrmAccountStatus.ACTIVE,  "2024-09-30")
        ));
    }

    private CrmAccount acc(String code, String name, String industry, AccountType type, int emp,
                            double rev, String owner, String phone, String website, String country,
                            CrmAccountStatus status, String createdAt) {
        return CrmAccount.builder()
                .accountCode(code).name(name).industry(industry).type(type)
                .employees(emp).annualRevenue(rev).owner(owner).phone(phone)
                .website(website).country(country).status(status)
                .createdAt(LocalDate.parse(createdAt)).build();
    }

    // ── Contacts ─────────────────────────────────────────────────────────────

    private void loadContacts(Map<String, CrmAccount> accMap) {
        contactRepo.saveAll(java.util.List.of(
            con("CON001","Marcus",  "Reid",      "VP of Operations",         "ACC001", accMap, "m.reid@acme.com",           "+1-555-0101","Sarah Kim",   "2026-03-18", CrmContactStatus.ACTIVE,  "2024-03-15"),
            con("CON002","Priya",   "Sharma",    "CTO",                      "ACC002", accMap, "p.sharma@technova.com",     "+1-555-0102","James Carter","2026-03-22", CrmContactStatus.ACTIVE,  "2024-05-20"),
            con("CON003","Carlos",  "Vega",      "Director of Procurement",  "ACC003", accMap, "c.vega@globalretail.com",   "+1-555-0103","Sarah Kim",   "2026-03-25", CrmContactStatus.ACTIVE,  "2025-01-10"),
            con("CON004","Aisha",   "Nwosu",     "CFO",                      "ACC004", accMap, "a.nwosu@bluesky.com",       "+1-555-0104","Mike Torres", "2026-03-10", CrmContactStatus.ACTIVE,  "2025-02-14"),
            con("CON005","Tom",     "Lindqvist", "CEO",                      "ACC005", accMap, "t.lindq@sunrise.com",       "+1-555-0105","James Carter","2026-03-28", CrmContactStatus.ACTIVE,  "2024-11-05"),
            con("CON006","Elena",   "Novak",     "Head of IT",               "ACC006", accMap, "e.novak@vertex.com",        "+1-555-0106","Mike Torres", "2026-02-20", CrmContactStatus.ACTIVE,  "2025-03-01"),
            con("CON007","Kwame",   "Asante",    "VP of Engineering",        "ACC007", accMap, "k.asante@nexus.com",        "+1-555-0107","Sarah Kim",   "2026-03-30", CrmContactStatus.ACTIVE,  "2024-07-18"),
            con("CON008","Sophie",  "Müller",    "Purchasing Manager",       "ACC008", accMap, "s.muller@orion.com",        "+1-555-0108","James Carter","2026-01-15", CrmContactStatus.INACTIVE,"2025-01-22"),
            con("CON009","Raj",     "Patel",     "Director of Technology",   "ACC009", accMap, "r.patel@pinnacle.com",      "+1-555-0109","Mike Torres", "2026-03-27", CrmContactStatus.ACTIVE,  "2025-03-08"),
            con("CON010","Nina",    "Bergström", "Supply Chain Director",    "ACC010", accMap, "n.berg@atlantic.com",       "+1-555-0110","Sarah Kim",   "2026-03-20", CrmContactStatus.ACTIVE,  "2024-09-30"),
            con("CON011","David",   "Chen",      "Operations Manager",       "ACC001", accMap, "d.chen@acme.com",           "+1-555-0111","Sarah Kim",   "2026-03-12", CrmContactStatus.ACTIVE,  "2024-06-01"),
            con("CON012","Laura",   "Sánchez",   "Head of Finance",          "ACC003", accMap, "l.sanchez@globalretail.com","+1-555-0112","James Carter","2026-03-05", CrmContactStatus.ACTIVE,  "2025-02-01")
        ));
    }

    private CrmContact con(String code, String first, String last, String title,
                            String accCode, Map<String, CrmAccount> accMap,
                            String email, String phone, String owner, String lastActivity,
                            CrmContactStatus status, String createdAt) {
        CrmAccount acc = accMap.get(accCode);
        return CrmContact.builder()
                .contactCode(code).firstName(first).lastName(last).title(title)
                .account(acc).company(acc != null ? acc.getName() : "")
                .email(email).phone(phone).owner(owner)
                .lastActivity(LocalDate.parse(lastActivity))
                .status(status).createdAt(LocalDate.parse(createdAt)).build();
    }

    // ── Opportunities ─────────────────────────────────────────────────────────

    private void loadOpportunities(Map<String, CrmAccount> accMap, Map<String, CrmContact> conMap) {
        oppRepo.saveAll(java.util.List.of(
            opp("OPP001","Acme ERP Expansion",           "ACC001","CON001",accMap,conMap,OpportunityStage.CLOSED_WON,      85000,  100,"2026-02-28","Sarah Kim",   "2026-01-10"),
            opp("OPP002","TechNova Cloud Migration",      "ACC002","CON002",accMap,conMap,OpportunityStage.VALUE_PROPOSITION,42000,  60, "2026-04-30","James Carter","2026-01-25"),
            opp("OPP003","GlobalRetail Platform Deal",    "ACC003","CON003",accMap,conMap,OpportunityStage.NEEDS_ANALYSIS,  130000, 45, "2026-05-15","Sarah Kim",   "2026-02-01"),
            opp("OPP004","BlueSky Finance Module",        "ACC004","CON004",accMap,conMap,OpportunityStage.QUALIFICATION,   25000,  30, "2026-06-01","Mike Torres", "2026-02-14"),
            opp("OPP005","Sunrise MES Integration",       "ACC005","CON005",accMap,conMap,OpportunityStage.DECISION,        200000, 75, "2026-04-15","James Carter","2026-02-20"),
            opp("OPP006","Vertex IT Consulting Package",  "ACC006","CON006",accMap,conMap,OpportunityStage.PROSPECTING,     15000,  20, "2026-07-01","Mike Torres", "2026-03-01"),
            opp("OPP007","Nexus Dev Tools License",       "ACC007","CON007",accMap,conMap,OpportunityStage.CLOSED_WON,      78000,  100,"2026-03-15","Sarah Kim",   "2026-03-05"),
            opp("OPP008","Orion Logistics Suite",         "ACC008","CON008",accMap,conMap,OpportunityStage.CLOSED_LOST,     55000,  0,  "2026-03-20","James Carter","2026-03-10"),
            opp("OPP009","PinnacleTech Analytics",        "ACC009","CON009",accMap,conMap,OpportunityStage.QUALIFICATION,   95000,  35, "2026-05-30","Mike Torres", "2026-03-15"),
            opp("OPP010","Atlantic Supply Chain Tracker", "ACC010","CON010",accMap,conMap,OpportunityStage.NEEDS_ANALYSIS,  32000,  40, "2026-06-15","Sarah Kim",   "2026-03-20"),
            opp("OPP011","Acme HR Full Module",           "ACC001","CON011",accMap,conMap,OpportunityStage.VALUE_PROPOSITION,120000, 65, "2026-05-01","Sarah Kim",   "2026-02-10"),
            opp("OPP012","GlobalRetail Finance Upgrade",  "ACC003","CON012",accMap,conMap,OpportunityStage.DECISION,        95000,  80, "2026-04-20","James Carter","2026-03-01")
        ));
    }

    private Opportunity opp(String code, String name, String accCode, String conCode,
                             Map<String, CrmAccount> accMap, Map<String, CrmContact> conMap,
                             OpportunityStage stage, double value, int prob,
                             String closeDate, String owner, String createdAt) {
        CrmAccount acc = accMap.get(accCode);
        CrmContact con = conMap.get(conCode);
        return Opportunity.builder()
                .opportunityCode(code).name(name)
                .account(acc).accountName(acc != null ? acc.getName() : "")
                .contact(con).contactName(con != null ? con.getFirstName() + " " + con.getLastName() : "")
                .stage(stage).value(BigDecimal.valueOf(value)).probability(prob)
                .closeDate(LocalDate.parse(closeDate)).owner(owner)
                .createdAt(LocalDate.parse(createdAt)).build();
    }

    // ── Activities ────────────────────────────────────────────────────────────

    private void loadActivities(Map<String, CrmContact> conMap) {
        activityRepo.saveAll(java.util.List.of(
            act("ACT001",CrmActivityType.CALL,    "Discovery call — ERP needs",          "CON003",conMap,"Sarah Kim",   "2026-03-25",ActivityOutcome.COMPLETED,       "Discussed 3 modules. Follow up with proposal next week."),
            act("ACT002",CrmActivityType.EMAIL,   "Sent pricing proposal",               "CON002",conMap,"James Carter","2026-03-22",ActivityOutcome.COMPLETED,       "Proposal for cloud migration package sent."),
            act("ACT003",CrmActivityType.MEETING, "On-site demo",                        "CON005",conMap,"James Carter","2026-03-28",ActivityOutcome.COMPLETED,       "Full product demo, very positive reaction. Decision expected in 2 weeks."),
            act("ACT004",CrmActivityType.CALL,    "Follow-up after proposal",            "CON004",conMap,"Mike Torres", "2026-03-10",ActivityOutcome.NO_ANSWER,        "Left voicemail, send email follow-up."),
            act("ACT005",CrmActivityType.DEMO,    "Analytics platform walkthrough",      "CON009",conMap,"Mike Torres", "2026-03-27",ActivityOutcome.COMPLETED,       "Showed reporting dashboards and AI insights. Very interested."),
            act("ACT006",CrmActivityType.EMAIL,   "Introduction email",                  "CON006",conMap,"Mike Torres", "2026-03-01",ActivityOutcome.COMPLETED,       "Initial outreach to schedule a discovery call."),
            act("ACT007",CrmActivityType.MEETING, "Contract negotiation",                "CON007",conMap,"Sarah Kim",   "2026-03-12",ActivityOutcome.COMPLETED,       "Terms agreed. Contract signed."),
            act("ACT008",CrmActivityType.CALL,    "Quarterly business review",           "CON001",conMap,"Sarah Kim",   "2026-03-18",ActivityOutcome.COMPLETED,       "Customer satisfied with ERP. Upsell opportunity identified."),
            act("ACT009",CrmActivityType.TASK,    "Prepare technical proposal",          "CON012",conMap,"James Carter","2026-04-01",ActivityOutcome.SCHEDULED,       "Need to include finance module pricing breakdown."),
            act("ACT010",CrmActivityType.CALL,    "Supply chain module overview",        "CON010",conMap,"Sarah Kim",   "2026-03-20",ActivityOutcome.COMPLETED,       "Intro to supply chain tracker. Requested sandbox access."),
            act("ACT011",CrmActivityType.EMAIL,   "Decision deadline reminder",          "CON011",conMap,"Sarah Kim",   "2026-03-29",ActivityOutcome.COMPLETED,       "Reminder sent for HR module decision."),
            act("ACT012",CrmActivityType.DEMO,    "Finance module live demo",            "CON003",conMap,"Sarah Kim",   "2026-03-30",ActivityOutcome.SCHEDULED,       "Demo scheduled for full executive team.")
        ));
    }

    private CrmActivity act(String code, CrmActivityType type, String subject,
                             String conCode, Map<String, CrmContact> conMap,
                             String owner, String date, ActivityOutcome outcome, String notes) {
        CrmContact con = conMap.get(conCode);
        return CrmActivity.builder()
                .activityCode(code).type(type).subject(subject)
                .contact(con)
                .contactName(con != null ? con.getFirstName() + " " + con.getLastName() : "")
                .accountName(con != null ? con.getCompany() : "")
                .owner(owner).date(LocalDate.parse(date))
                .outcome(outcome).notes(notes).build();
    }

    // ── Support Cases ─────────────────────────────────────────────────────────

    private void loadCases(Map<String, CrmAccount> accMap) {
        caseRepo.saveAll(java.util.List.of(
            sc("CASE001","Data import failure on inventory module",       "ACC001",accMap,"Marcus Reid",    CaseStatus.IN_PROGRESS,      CasePriority.HIGH,    "Sarah Kim",   "2026-03-20","2026-03-25","Customer reports that bulk CSV import fails with 500+ rows."),
            sc("CASE002","Report export producing blank PDFs",            "ACC007",accMap,"Kwame Asante",   CaseStatus.OPEN,             CasePriority.MEDIUM,  "James Carter","2026-03-22","2026-03-22","Finance reports export as blank when date range exceeds 90 days."),
            sc("CASE003","User permissions not applying correctly",       "ACC002",accMap,"Priya Sharma",   CaseStatus.PENDING_CUSTOMER, CasePriority.HIGH,    "Mike Torres", "2026-03-15","2026-03-28","New users created with admin role have no access to HR module."),
            sc("CASE004","Email notification delays",                     "ACC005",accMap,"Tom Lindqvist",  CaseStatus.RESOLVED,         CasePriority.LOW,     "Sarah Kim",   "2026-03-08","2026-03-18","Approval workflow emails delayed by 15–30 minutes."),
            sc("CASE005","Dashboard KPIs showing incorrect totals",       "ACC010",accMap,"Nina Bergström", CaseStatus.OPEN,             CasePriority.CRITICAL,"James Carter","2026-03-28","2026-03-28","Monthly revenue chart shows double the actual value since last update."),
            sc("CASE006","Integration sync failure with accounting sw",   "ACC003",accMap,"Carlos Vega",    CaseStatus.IN_PROGRESS,      CasePriority.HIGH,    "Mike Torres", "2026-03-24","2026-03-29","QuickBooks sync fails at midnight daily job."),
            sc("CASE007","Mobile app cannot load on iOS 17",              "ACC009",accMap,"Raj Patel",      CaseStatus.OPEN,             CasePriority.MEDIUM,  "Sarah Kim",   "2026-03-26","2026-03-26","App freezes on login screen on devices running iOS 17.3+."),
            sc("CASE008","Payroll export to bank rejecting file format",  "ACC004",accMap,"Aisha Nwosu",    CaseStatus.CLOSED,           CasePriority.HIGH,    "James Carter","2026-03-01","2026-03-10","ACH file format was outdated. Fixed by updating export template.")
        ));
    }

    private SupportCase sc(String code, String subject, String accCode,
                            Map<String, CrmAccount> accMap, String contactName,
                            CaseStatus status, CasePriority priority, String assignedTo,
                            String createdAt, String updatedAt, String description) {
        CrmAccount acc = accMap.get(accCode);
        return SupportCase.builder()
                .caseCode(code).subject(subject)
                .account(acc).accountName(acc != null ? acc.getName() : "")
                .contactName(contactName).status(status).priority(priority)
                .assignedTo(assignedTo)
                .createdAt(LocalDate.parse(createdAt)).updatedAt(LocalDate.parse(updatedAt))
                .description(description).build();
    }
}

