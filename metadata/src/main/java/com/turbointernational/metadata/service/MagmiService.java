package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.service.Mas90Service.TURBO_INTERNATIONAL_MANUFACTURER_ID;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.InterchangeDao;
import com.turbointernational.metadata.web.dto.mas90.ArInvoiceHistoryDetailDto;
import com.turbointernational.metadata.web.dto.mas90.ArInvoiceHistoryHeaderDto;
import com.turbointernational.metadata.web.dto.mas90.InvoiceDto;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class MagmiService {

    private final static Logger log = LoggerFactory.getLogger(MagmiService.class);

    private final static String QUERY_INVOICEHISTORYHEADER = "SELECT INVOICENO, HEADERSEQNO, MODULECODE, INVOICETYPE, "
            + "INVOICEDATE, TRANSACTIONDATE, ARDIVISIONNO, CUSTOMERNO, TERMSCODE, TAXSCHEDULE, TAXEXEMPTNO, "
            + "SALESPERSONDIVISIONNO, SALESPERSONNO, CUSTOMERPONO, APPLYTOINVOICENO, COMMENT, REPETITIVEINVOICEREFNO, "
            + "JOBNO, INVOICEDUEDATE, DISCOUNTDUEDATE, SOURCEJOURNAL, JOURNALNOGLBATCHNO, BATCHFAX, FAXNO, "
            + "SHIPPINGINVOICE, SALESORDERNO, ORDERTYPE, ORDERDATE, BILLTONAME, BILLTOADDRESS1, "
            + "BILLTOADDRESS2, BILLTOADDRESS3, BILLTOCITY, BILLTOSTATE, BILLTOZIPCODE, "
            + "BILLTOCOUNTRYCODE, SHIPTOCODE, SHIPTONAME, SHIPTOADDRESS1, SHIPTOADDRESS2, "
            + "SHIPTOADDRESS3, SHIPTOCITY, SHIPTOSTATE, SHIPTOZIPCODE, SHIPTOCOUNTRYCODE, SHIPDATE, "
            + "SHIPVIA, SHIPZONE, FOB, CONFIRMTO, CHECKNOFORDEPOSIT, SPLITCOMMISSIONS, "
            + "SALESPERSONDIVISIONNO2, SALESPERSONNO2, SALESPERSONDIVISIONNO3, SALESPERSONNO3, "
            + "SALESPERSONDIVISIONNO4, SALESPERSONNO4, SALESPERSONDIVISIONNO5, SALESPERSONNO5, "
            + "PAYMENTTYPE, PAYMENTTYPECATEGORY, OTHERPAYMENTTYPEREFNO, RMANO, EBMSUBMISSIONTYPE, "
            + "EBMUSERIDSUBMITTINGTHISORDER, EBMUSERTYPE, SHIPPERID, USERKEY, WAREHOUSECODE, "
            + "SHIPWEIGHT, RESIDENTIALADDRESS, EMAILADDRESS, CRMUSERID, CRMCOMPANYID, CRMPERSONID, "
            + "CRMOPPORTUNITYID, TAXABLESALESAMT, NONTAXABLESALESAMT, FREIGHTAMT, SALESTAXAMT, "
            + "COSTOFSALESAMT, AMOUNTSUBJECTTODISCOUNT, DISCOUNTRATE, DISCOUNTAMT, "
            + "SALESSUBJECTTOCOMM, COSTSUBJECTTOCOMM, COMMISSIONRATE, COMMISSIONAMT, "
            + "SPLITCOMMRATE2, SPLITCOMMRATE3, SPLITCOMMRATE4, SPLITCOMMRATE5, DEPOSITAMT, "
            + "WEIGHT, RETENTIONAMT, NUMBEROFPACKAGES, DATEUPDATED, TIMEUPDATED, USERUPDATEDKEY, "
            + "ARMC_234_ENTRYCURRENCY, ARMC_234_ENTRYRATE, ARMC_234_BASECOSTOFSALESAMT, "
            + "ARMC_234_BASECOSTSUBJECTTOCOMM, ARMC_234_BASECOMMISSIONAMT, BILLTODIVISIONNO, "
            + "BILLTOCUSTOMERNO FROM AR_INVOICEHISTORYHEADER WHERE ";

    private final static String QUERY_INVOICEHISTORYDETAIL = "SELECT INVOICENO, HEADERSEQNO, DETAILSEQNO, ITEMCODE, "
            + "ITEMTYPE, ITEMCODEDESC, EXTENDEDDESCRIPTIONKEY, SALESACCTKEY, COSTOFGOODSSOLDACCTKEY, "
            + "INVENTORYACCTKEY, UNITOFMEASURE, SUBJECTTOEXEMPTION, COMMISSIONABLE, TAXCLASS, DISCOUNT, DROPSHIP, "
            + "WAREHOUSECODE, PRICELEVEL, PRODUCTLINE, VALUATION, PRICEOVERRIDDEN, ORDERWAREHOUSE, "
            + "REVISION, BILLOPTION1, BILLOPTION2, BILLOPTION3, BILLOPTION4, BILLOPTION5, "
            + "BILLOPTION6, BILLOPTION7, BILLOPTION8, BILLOPTION9, KITITEM, EXPLODEDKITITEM, "
            + "SKIPPRINTCOMPLINE, STANDARDKITBILL, ALIASITEMNO, CUSTOMERACTION, ITEMACTION, "
            + "WARRANTYCODE, EXPIRATIONDATE, EXPIRATIONOVERRIDDEN, COSTCODE, COSTTYPE, COMMENTTEXT, "
            + "PROMISEDATE, QUANTITYSHIPPED, QUANTITYORDERED, QUANTITYBACKORDERED, UNITPRICE, "
            + "UNITCOST, UNITOFMEASURECONVFACTOR, COMMISSIONAMT, LINEDISCOUNTPERCENT, "
            + "QUANTITYPERBILL, EXTENSIONAMT, ARMC_234_ENTRYCURRENCY, ARMC_234_ENTRYRATE, "
            + "ARMC_234_BASEUNITCOST, ARMC_234_BASECOMMISSIONAMT, APDIVISIONNO, VENDORNO, "
            + "PURCHASEORDERNO, PURCHASEORDERREQUIREDDATE, COMMODITYCODE, ALTERNATETAXIDENTIFIER, "
            + "TAXTYPEAPPLIED, NETGROSSINDICATOR, DEBITCREDITINDICATOR, TAXAMT, TAXRATE "
            + "FROM AR_INVOICEHISTORYDETAIL WHERE ";

    @Autowired
    private DataSource dataSource;

    @Qualifier("dataSourceMas90")
    @Autowired
    private DataSource dataSourceMas90; // connections to MS-SQL (MAS90)

    private JdbcTemplate db;

    private JdbcTemplate mas90db;

    @Autowired
    private Mas90Service mas90Service;

    @Autowired
    private InterchangeDao interchangeDao;

    @PostConstruct
    public void init() {
        db = new JdbcTemplate(dataSource);
        mas90db = new JdbcTemplate(dataSourceMas90);
    }

    public List<ArInvoiceHistoryHeaderDto> getInvoiceHistoryHeader(List<ArInvoiceHistoryHeaderDto.Key> request) {
        List<ArInvoiceHistoryHeaderDto> retVal = new ArrayList<>(request.size());
        for (ArInvoiceHistoryHeaderDto.Key key : request) {
            String customerno = key.getCUSTOMERNO();
            Long invoicedate = key.getINVOICEDATE();
            String invoiceno = key.getINVOICENO();
            String sql = QUERY_INVOICEHISTORYHEADER;
            if (customerno == null) {
                sql += " CUSTOMERNO IS NULL ";
            } else {
                sql += " CUSTOMERNO=? ";
            }
            if (invoicedate == null) {
                sql += " AND INVOICEDATE IS NULL ";
            } else {
                sql += " AND INVOICEDATE=? ";
            }
            if (invoiceno == null) {
                sql += " AND INVOICENO IS NULL ";
            } else {
                sql += " AND INVOICENO =? ";
            }
            List<ArInvoiceHistoryHeaderDto.Record> records = mas90db.query(sql, pss -> {
                int parameterIndex = 1;
                if (customerno != null) {
                    pss.setString(parameterIndex++, customerno);
                }
                if (invoicedate != null) {
                    pss.setDate(parameterIndex++, new java.sql.Date(invoicedate));
                }
                if (invoiceno != null) {
                    pss.setString(parameterIndex, invoiceno);
                }
            }, (rs, rowNum) -> {
                String INVOICENO = rs.getString(1);
                String HEADERSEQNO = rs.getString(2);
                String MODULECODE = rs.getString(3);
                String INVOICETYPE = rs.getString(4);
                Date INVOICEDATE = rs.getDate(5);
                Date TRANSACTIONDATE = rs.getDate(6);
                String ARDIVISIONNO = rs.getString(7);
                String CUSTOMERNO = rs.getString(8);
                String TERMSCODE = rs.getString(9);
                String TAXSCHEDULE = rs.getString(10);
                String TAXEXEMPTNO = rs.getString(11);
                String SALESPERSONDIVISIONNO = rs.getString(12);
                String SALESPERSONNO = rs.getString(13);
                String CUSTOMERPONO = rs.getString(14);
                String APPLYTOINVOICENO = rs.getString(15);
                String COMMENT = rs.getString(16);
                String REPETITIVEINVOICEREFNO = rs.getString(17);
                String JOBNO = rs.getString(18);
                String INVOICEDUEDATE = rs.getString(19);
                String DISCOUNTDUEDATE = rs.getString(20);
                String SOURCEJOURNAL = rs.getString(21);
                String JOURNALNOGLBATCHNO = rs.getString(22);
                String BATCHFAX = rs.getString(23);
                String FAXNO = rs.getString(24);
                String SHIPPINGINVOICE = rs.getString(25);
                String SALESORDERNO = rs.getString(26);
                String ORDERTYPE = rs.getString(27);
                String ORDERDATE = rs.getString(28);
                String BILLTONAME = rs.getString(29);
                String BILLTOADDRESS1 = rs.getString(30);
                String BILLTOADDRESS2 = rs.getString(31);
                String BILLTOADDRESS3 = rs.getString(32);
                String BILLTOCITY = rs.getString(33);
                String BILLTOSTATE = rs.getString(34);
                String BILLTOZIPCODE = rs.getString(35);
                String BILLTOCOUNTRYCODE = rs.getString(36);
                String SHIPTOCODE = rs.getString(37);
                String SHIPTONAME = rs.getString(38);
                String SHIPTOADDRESS1 = rs.getString(39);
                String SHIPTOADDRESS2 = rs.getString(40);
                String SHIPTOADDRESS3 = rs.getString(41);
                String SHIPTOCITY = rs.getString(42);
                String SHIPTOSTATE = rs.getString(43);
                String SHIPTOZIPCODE = rs.getString(44);
                String SHIPTOCOUNTRYCODE = rs.getString(45);
                Date SHIPDATE = rs.getDate(46);
                String SHIPVIA = rs.getString(47);
                String SHIPZONE = rs.getString(48);
                String FOB = rs.getString(49);
                String CONFIRMTO = rs.getString(50);
                String CHECKNOFORDEPOSIT = rs.getString(51);
                String SPLITCOMMISSIONS = rs.getString(52);
                String SALESPERSONDIVISIONNO2 = rs.getString(53);
                String SALESPERSONNO2 = rs.getString(54);
                String SALESPERSONDIVISIONNO3 = rs.getString(55);
                String SALESPERSONNO3 = rs.getString(56);
                String SALESPERSONDIVISIONNO4 = rs.getString(57);
                String SALESPERSONNO4 = rs.getString(58);
                String SALESPERSONDIVISIONNO5 = rs.getString(59);
                String SALESPERSONNO5 = rs.getString(60);
                String PAYMENTTYPE = rs.getString(61);
                String PAYMENTTYPECATEGORY = rs.getString(62);
                String OTHERPAYMENTTYPEREFNO = rs.getString(63);
                String RMANO = rs.getString(64);
                String EBMSUBMISSIONTYPE = rs.getString(65);
                String EBMUSERIDSUBMITTINGTHISORDER = rs.getString(66);
                String EBMUSERTYPE = rs.getString(67);
                String SHIPPERID = rs.getString(68);
                String USERKEY = rs.getString(69);
                String WAREHOUSECODE = rs.getString(70);
                String SHIPWEIGHT = rs.getString(71);
                String RESIDENTIALADDRESS = rs.getString(72);
                String EMAILADDRESS = rs.getString(73);
                String CRMUSERID = rs.getString(74);
                String CRMCOMPANYID = rs.getString(75);
                String CRMPERSONID = rs.getString(76);
                String CRMOPPORTUNITYID = rs.getString(77);
                Double TAXABLESALESAMT = rs.getDouble(78);
                Double NONTAXABLESALESAMT = rs.getDouble(79);
                Double FREIGHTAMT = rs.getDouble(80);
                Double SALESTAXAMT = rs.getDouble(81);
                Double COSTOFSALESAMT = rs.getDouble(82);
                Double AMOUNTSUBJECTTODISCOUNT = rs.getDouble(83);
                Double DISCOUNTRATE = rs.getDouble(84);
                Double DISCOUNTAMT = rs.getDouble(85);
                Double SALESSUBJECTTOCOMM = rs.getDouble(86);
                Double COSTSUBJECTTOCOMM = rs.getDouble(87);
                Double COMMISSIONRATE = rs.getDouble(88);
                Double COMMISSIONAMT = rs.getDouble(89);
                Double SPLITCOMMRATE2 = rs.getDouble(90);
                Double SPLITCOMMRATE3 = rs.getDouble(91);
                Double SPLITCOMMRATE4 = rs.getDouble(92);
                Double SPLITCOMMRATE5 = rs.getDouble(93);
                Double DEPOSITAMT = rs.getDouble(94);
                Double WEIGHT = rs.getDouble(95);
                Double RETENTIONAMT = rs.getDouble(96);
                Double NUMBEROFPACKAGES = rs.getDouble(97);
                Date DATEUPDATED = rs.getDate(98);
                String TIMEUPDATED = rs.getString(99);
                String USERUPDATEDKEY = rs.getString(100);
                String ARMC_234_ENTRYCURRENCY = rs.getString(101);
                Double ARMC_234_ENTRYRATE = rs.getDouble(102);
                Double ARMC_234_BASECOSTOFSALESAMT = rs.getDouble(103);
                Double ARMC_234_BASECOSTSUBJECTTOCOMM = rs.getDouble(104);
                Double ARMC_234_BASECOMMISSIONAMT = rs.getDouble(105);
                String BILLTODIVISIONNO = rs.getString(106);
                String BILLTOCUSTOMERNO = rs.getString(107);
                ArInvoiceHistoryHeaderDto.Record record = new ArInvoiceHistoryHeaderDto.Record(INVOICENO, HEADERSEQNO,
                        MODULECODE, INVOICETYPE, INVOICEDATE, TRANSACTIONDATE, ARDIVISIONNO, CUSTOMERNO, TERMSCODE,
                        TAXSCHEDULE, TAXEXEMPTNO, SALESPERSONDIVISIONNO, SALESPERSONNO, CUSTOMERPONO, APPLYTOINVOICENO,
                        COMMENT, REPETITIVEINVOICEREFNO, JOBNO, INVOICEDUEDATE, DISCOUNTDUEDATE, SOURCEJOURNAL,
                        JOURNALNOGLBATCHNO, BATCHFAX, FAXNO, SHIPPINGINVOICE, SALESORDERNO, ORDERTYPE, ORDERDATE,
                        BILLTONAME, BILLTOADDRESS1, BILLTOADDRESS2, BILLTOADDRESS3, BILLTOCITY, BILLTOSTATE,
                        BILLTOZIPCODE, BILLTOCOUNTRYCODE, SHIPTOCODE, SHIPTONAME, SHIPTOADDRESS1, SHIPTOADDRESS2,
                        SHIPTOADDRESS3, SHIPTOCITY, SHIPTOSTATE, SHIPTOZIPCODE, SHIPTOCOUNTRYCODE, SHIPDATE, SHIPVIA,
                        SHIPZONE, FOB, CONFIRMTO, CHECKNOFORDEPOSIT, SPLITCOMMISSIONS, SALESPERSONDIVISIONNO2,
                        SALESPERSONNO2, SALESPERSONDIVISIONNO3, SALESPERSONNO3, SALESPERSONDIVISIONNO4, SALESPERSONNO4,
                        SALESPERSONDIVISIONNO5, SALESPERSONNO5, PAYMENTTYPE, PAYMENTTYPECATEGORY, OTHERPAYMENTTYPEREFNO,
                        RMANO, EBMSUBMISSIONTYPE, EBMUSERIDSUBMITTINGTHISORDER, EBMUSERTYPE, SHIPPERID, USERKEY,
                        WAREHOUSECODE, SHIPWEIGHT, RESIDENTIALADDRESS, EMAILADDRESS, CRMUSERID, CRMCOMPANYID,
                        CRMPERSONID, CRMOPPORTUNITYID, TAXABLESALESAMT, NONTAXABLESALESAMT, FREIGHTAMT, SALESTAXAMT,
                        COSTOFSALESAMT, AMOUNTSUBJECTTODISCOUNT, DISCOUNTRATE, DISCOUNTAMT, SALESSUBJECTTOCOMM,
                        COSTSUBJECTTOCOMM, COMMISSIONRATE, COMMISSIONAMT, SPLITCOMMRATE2, SPLITCOMMRATE3,
                        SPLITCOMMRATE4, SPLITCOMMRATE5, DEPOSITAMT, WEIGHT, RETENTIONAMT, NUMBEROFPACKAGES, DATEUPDATED,
                        TIMEUPDATED, USERUPDATEDKEY, ARMC_234_ENTRYCURRENCY, ARMC_234_ENTRYRATE,
                        ARMC_234_BASECOSTOFSALESAMT, ARMC_234_BASECOSTSUBJECTTOCOMM, ARMC_234_BASECOMMISSIONAMT,
                        BILLTODIVISIONNO, BILLTOCUSTOMERNO);
                return record;
            });
            retVal.add(new ArInvoiceHistoryHeaderDto(key, records));
        }
        return retVal;
    }

    public List<ArInvoiceHistoryDetailDto> getInvoiceHistoryDetail(List<ArInvoiceHistoryDetailDto.Key> request) {
        List<ArInvoiceHistoryDetailDto> retVal = new ArrayList<>(request.size());
        for (ArInvoiceHistoryDetailDto.Key key : request) {
            String invoiceno = key.getINVOICENO();
            String itemcode = key.getITEMCODE();
            String sql = QUERY_INVOICEHISTORYDETAIL;
            if (invoiceno == null) {
                sql += " INVOICENO IS NULL ";
            } else {
                sql += " INVOICENO=? ";
            }
            if (itemcode == null) {
                sql += " AND ITEMCODE IS NULL ";
            } else {
                sql += " AND ITEMCODE=? ";
            }
            List<ArInvoiceHistoryDetailDto.Record> records = mas90db.query(sql, pss -> {
                int parameterIndex = 1;
                if (invoiceno != null) {
                    pss.setString(parameterIndex++, invoiceno);
                }
                if (itemcode != null) {
                    pss.setString(parameterIndex, itemcode);
                }
            }, (rs, rowNum) -> {
                String INVOICENO = rs.getString(1);
                String HEADERSEQNO = rs.getString(2);
                String DETAILSEQNO = rs.getString(3);
                String ITEMCODE = rs.getString(4);
                String ITEMTYPE = rs.getString(5);
                String ITEMCODEDESC = rs.getString(6);
                String EXTENDEDDESCRIPTIONKEY = rs.getString(7);
                String SALESACCTKEY = rs.getString(8);
                String COSTOFGOODSSOLDACCTKEY = rs.getString(9);
                String INVENTORYACCTKEY = rs.getString(10);
                String UNITOFMEASURE = rs.getString(11);
                String SUBJECTTOEXEMPTION = rs.getString(12);
                String COMMISSIONABLE = rs.getString(13);
                String TAXCLASS = rs.getString(14);
                String DISCOUNT = rs.getString(15);
                String DROPSHIP = rs.getString(16);
                String WAREHOUSECODE = rs.getString(17);
                String PRICELEVEL = rs.getString(18);
                String PRODUCTLINE = rs.getString(19);
                String VALUATION = rs.getString(20);
                String PRICEOVERRIDDEN = rs.getString(21);
                String ORDERWAREHOUSE = rs.getString(22);
                String REVISION = rs.getString(23);
                String BILLOPTION1 = rs.getString(24);
                String BILLOPTION2 = rs.getString(25);
                String BILLOPTION3 = rs.getString(26);
                String BILLOPTION4 = rs.getString(27);
                String BILLOPTION5 = rs.getString(28);
                String BILLOPTION6 = rs.getString(29);
                String BILLOPTION7 = rs.getString(30);
                String BILLOPTION8 = rs.getString(31);
                String BILLOPTION9 = rs.getString(32);
                String KITITEM = rs.getString(33);
                String EXPLODEDKITITEM = rs.getString(34);
                String SKIPPRINTCOMPLINE = rs.getString(35);
                String STANDARDKITBILL = rs.getString(36);
                String ALIASITEMNO = rs.getString(37);
                String CUSTOMERACTION = rs.getString(38);
                String ITEMACTION = rs.getString(39);
                String WARRANTYCODE = rs.getString(40);
                Date EXPIRATIONDATE = rs.getDate(41);
                String EXPIRATIONOVERRIDDEN = rs.getString(42);
                String COSTCODE = rs.getString(43);
                String COSTTYPE = rs.getString(44);
                String COMMENTTEXT = rs.getString(45);
                Date PROMISEDATE = rs.getDate(46);
                Double QUANTITYSHIPPED = rs.getDouble(47);
                Double QUANTITYORDERED = rs.getDouble(48);
                Double QUANTITYBACKORDERED = rs.getDouble(49);
                Double UNITPRICE = rs.getDouble(50);
                Double UNITCOST = rs.getDouble(51);
                Double UNITOFMEASURECONVFACTOR = rs.getDouble(52);
                Double COMMISSIONAMT = rs.getDouble(53);
                Double LINEDISCOUNTPERCENT = rs.getDouble(54);
                Double QUANTITYPERBILL = rs.getDouble(55);
                Double EXTENSIONAMT = rs.getDouble(56);
                String ARMC_234_ENTRYCURRENCY = rs.getString(57);
                Double ARMC_234_ENTRYRATE = rs.getDouble(58);
                Double ARMC_234_BASEUNITCOST = rs.getDouble(59);
                Double ARMC_234_BASECOMMISSIONAMT = rs.getDouble(60);
                String APDIVISIONNO = rs.getString(61);
                String VENDORNO = rs.getString(62);
                String PURCHASEORDERNO = rs.getString(63);
                Date PURCHASEORDERREQUIREDDATE = rs.getDate(64);
                String COMMODITYCODE = rs.getString(65);
                String ALTERNATETAXIDENTIFIER = rs.getString(66);
                String TAXTYPEAPPLIED = rs.getString(67);
                String NETGROSSINDICATOR = rs.getString(68);
                String DEBITCREDITINDICATOR = rs.getString(69);
                Double TAXAMT = rs.getDouble(70);
                Double TAXRATE = rs.getDouble(71);
                ArInvoiceHistoryDetailDto.Record record = new ArInvoiceHistoryDetailDto.Record(INVOICENO, HEADERSEQNO,
                        DETAILSEQNO, ITEMCODE, ITEMTYPE, ITEMCODEDESC, EXTENDEDDESCRIPTIONKEY, SALESACCTKEY,
                        COSTOFGOODSSOLDACCTKEY, INVENTORYACCTKEY, UNITOFMEASURE, SUBJECTTOEXEMPTION, COMMISSIONABLE,
                        TAXCLASS, DISCOUNT, DROPSHIP, WAREHOUSECODE, PRICELEVEL, PRODUCTLINE, VALUATION,
                        PRICEOVERRIDDEN, ORDERWAREHOUSE, REVISION, BILLOPTION1, BILLOPTION2, BILLOPTION3, BILLOPTION4,
                        BILLOPTION5, BILLOPTION6, BILLOPTION7, BILLOPTION8, BILLOPTION9, KITITEM, EXPLODEDKITITEM,
                        SKIPPRINTCOMPLINE, STANDARDKITBILL, ALIASITEMNO, CUSTOMERACTION, ITEMACTION, WARRANTYCODE,
                        EXPIRATIONDATE, EXPIRATIONOVERRIDDEN, COSTCODE, COSTTYPE, COMMENTTEXT, PROMISEDATE,
                        QUANTITYSHIPPED, QUANTITYORDERED, QUANTITYBACKORDERED, UNITPRICE, UNITCOST,
                        UNITOFMEASURECONVFACTOR, COMMISSIONAMT, LINEDISCOUNTPERCENT, QUANTITYPERBILL, EXTENSIONAMT,
                        ARMC_234_ENTRYCURRENCY, ARMC_234_ENTRYRATE, ARMC_234_BASEUNITCOST, ARMC_234_BASECOMMISSIONAMT,
                        APDIVISIONNO, VENDORNO, PURCHASEORDERNO, PURCHASEORDERREQUIREDDATE, COMMODITYCODE,
                        ALTERNATETAXIDENTIFIER, TAXTYPEAPPLIED, NETGROSSINDICATOR, DEBITCREDITINDICATOR, TAXAMT,
                        TAXRATE);
                return record;
            });
            retVal.add(new ArInvoiceHistoryDetailDto(key, records));
        }
        return retVal;
    }

    public List<InvoiceDto> getInvoiceHistory(Long beginMillis, int limitDays) throws SQLException {
        class PartDescriptor {

            private final Long partId;

            private final String partTypeName;

            PartDescriptor(Long partId, String partTypeName) {
                this.partId = partId;
                this.partTypeName = partTypeName;
            }

            public Long getPartId() {
                return partId;
            }

            public String getPartTypeName() {
                return partTypeName;
            }
        }
        ;
        if (limitDays < 0) {
            throw new IllegalArgumentException("Parameter 'limitDays' can't be negative: " + limitDays);
        }
        List<InvoiceDto> retVal = new ArrayList<>(300);
        java.sql.Date startDate, finishDate;
        if (beginMillis == null) {
            startDate = mas90db.queryForObject("select min(dateupdated) from ar_invoicehistoryheader",
                    java.sql.Date.class);
            if (startDate == null) {
                log.warn("Start date not found.");
                return retVal;
            }
            beginMillis = startDate.getTime();
        } else {
            startDate = new java.sql.Date(beginMillis);
        }
        finishDate = new java.sql.Date(beginMillis + limitDays * 3600 * 24 * 1000);
        Connection con = dataSourceMas90.getConnection();
        try {
            // @formatter:off
            PreparedStatement ps = con.prepareStatement(
                "select h.invoiceno, h.headerseqno, h.invoicedate, h.dateupdated, h.customerno, d.itemcode, " +
                "  d.itemcodedesc " +
                "from ar_invoicehistoryheader h " +
                "  join ar_invoicehistorydetail d on h.invoiceno = d.invoiceno and h.headerseqno = d.headerseqno " +
                "where h.dateupdated between ? and ? " +
                "order by h.dateupdated, h.invoiceno, h.headerseqno, d.detailseqno asc",
                TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
            // @formatter:on
            ps.setDate(1, startDate);
            ps.setDate(2, finishDate);
            ResultSet rs = ps.executeQuery();
            try {
                InvoiceDto dto = null;
                String invoiceno = null;
                String headerseqno = null;
                Date invoicedate = null;
                Date dateupdated = null;
                String customerno = null;
                String itemcode = null;
                String itemcodedesc = null;
                List<InvoiceDto.DetailsDto> details = null;
                Long partId = null;
                List<Long> interchanges = null;
                while (rs.next()) {
                    invoiceno = rs.getString(1);
                    headerseqno = rs.getString(2);
                    invoicedate = rs.getDate(3);
                    dateupdated = rs.getDate(4);
                    customerno = rs.getString(5);
                    itemcode = rs.getString(6);
                    itemcodedesc = rs.getString(7);

                    PartDescriptor pd;
                    if (!mas90Service.isManfrNum(itemcode)) {
                        // Skip unsuitable part numbers.
                        continue;
                    } else {
                        // @formatter:off
                        pd = db.query(
                          con2 -> con2.prepareStatement(
                            "select p.id, pt.name " +
                            "from part p join part_type pt on p.part_type_id=pt.id " +
                            "where p.manfr_id = " + TURBO_INTERNATIONAL_MANUFACTURER_ID + " and p.manfr_part_num = ?"),
                          ps2 -> {
                              ps2.setString(1, rs.getString(6) /* itemcode */);
                          },
                          rs2 -> {
                            if (rs2.next()) {
                                return new PartDescriptor(rs2.getLong(1), rs2.getString(2));
                            } else {
                                return null;
                            }
                        });
                        // @formatter:on
                        if (pd == null) {
                            // Part not found.
                            continue;
                        }
                    }
                    partId = pd.getPartId();
                    interchanges = interchangeDao.getInterchanges(partId);
                    InvoiceDto.DetailsDto dd = new InvoiceDto.DetailsDto(partId, itemcode, pd.getPartTypeName(),
                            interchanges, itemcodedesc);
                    if (dto == null || !dto.getNo().equals(invoiceno) || !dto.getHeaderSeqNo().equals(headerseqno)) {
                        retVal.add(dto);
                        details = new ArrayList<>(10);
                        dto = new InvoiceDto(invoiceno, headerseqno, invoicedate == null ? null : invoicedate.getTime(),
                                dateupdated == null ? null : dateupdated.getTime(), customerno, details);
                    }
                    details.add(dd);
                }
                if (dto != null) {
                    retVal.add(dto);
                }
            } finally {
                rs.close();
            }
        } finally {
            con.close();
        }
        return retVal;
    }

}
