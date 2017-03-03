package com.turbointernational.metadata.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.web.dto.mas90.ArInvoiceHistoryDetailDto;
import com.turbointernational.metadata.web.dto.mas90.ArInvoiceHistoryHeaderDto;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class MagmiService {

    @Qualifier("dataSourceMas90")
    @Autowired
    private DataSource dataSourceMas90; // connections to MS-SQL (MAS90)

    private JdbcTemplate mas90db;

    @PostConstruct
    public void init() {
        mas90db = new JdbcTemplate(dataSourceMas90);
    }

    public List<ArInvoiceHistoryHeaderDto> getInvoiceHistoryHeader(List<ArInvoiceHistoryHeaderDto.Key> request) {
        List<ArInvoiceHistoryHeaderDto> retVal = new ArrayList<>(request.size());
        for (ArInvoiceHistoryHeaderDto.Key key : request) {
            List<ArInvoiceHistoryHeaderDto.Record> records = mas90db
                    .query("SELECT INVOICENO, HEADERSEQNO, MODULECODE, INVOICETYPE, INVOICEDATE, TRANSACTIONDATE, "
                            + "ARDIVISIONNO, CUSTOMERNO, TERMSCODE, TAXSCHEDULE, TAXEXEMPTNO, SALESPERSONDIVISIONNO, "
                            + "SALESPERSONNO, CUSTOMERPONO, APPLYTOINVOICENO, COMMENT, REPETITIVEINVOICEREFNO, JOBNO, "
                            + "INVOICEDUEDATE, DISCOUNTDUEDATE, SOURCEJOURNAL, JOURNALNOGLBATCHNO, BATCHFAX, FAXNO, "
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
                            + "BILLTOCUSTOMERNO FROM AR_INVOICEHISTORYHEADER "
                            + "WHERE CUSTOMERNO=? AND INVOICEDATE=? AND INVOICENO=?", pss -> {
                                pss.setString(1, key.getCUSTOMERNO());
                                pss.setDate(2, new java.sql.Date(key.getINVOICEDATE().getTime()));
                                pss.setString(3, key.getINVOICENO());
                            }, (rs, num) -> {
                                String INVOICENO = rs.getString(1);
                                String HEADERSEQNO = rs.getString(2);
                                String MODULECODE = rs.getString(3);
                                String INVOICETYPE = rs.getString(4);
                                java.sql.Date INVOICEDATE = rs.getDate(5);
                                java.sql.Date TRANSACTIONDATE = rs.getDate(6);
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
                                java.sql.Date SHIPDATE = rs.getDate(46);
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
                                java.sql.Date DATEUPDATED = rs.getDate(98);
                                String TIMEUPDATED = rs.getString(99);
                                String USERUPDATEDKEY = rs.getString(100);
                                String ARMC_234_ENTRYCURRENCY = rs.getString(101);
                                java.sql.Date ARMC_234_ENTRYRATE = rs.getDate(102);
                                java.sql.Date ARMC_234_BASECOSTOFSALESAMT = rs.getDate(103);
                                java.sql.Date ARMC_234_BASECOSTSUBJECTTOCOMM = rs.getDate(104);
                                java.sql.Date ARMC_234_BASECOMMISSIONAMT = rs.getDate(105);
                                String BILLTODIVISIONNO = rs.getString(106);
                                String BILLTOCUSTOMERNO = rs.getString(107);
                                // TODO: ArInvoiceHistoryHeaderDto.Record record
                                // = new ArInvoiceHistoryHeaderDto.Record();
                                return null; // TODO
                            });
            retVal.add(new ArInvoiceHistoryHeaderDto(key, records));
        }
        return retVal;

    }

    public List<ArInvoiceHistoryDetailDto> getInvoiceHistoryDetail(List<ArInvoiceHistoryDetailDto.Key> request) {
        return null;
    }

}
