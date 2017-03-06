/**
 * DTO for the table AR_INVOICEHISTORYHEADER in the MsSql server.
 */
package com.turbointernational.metadata.web.dto.mas90;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(NON_NULL)
public class ArInvoiceHistoryHeaderDto {

    @JsonInclude(ALWAYS)
    public static class Key {

        public Key() {
        }

        public String getINVOICENO() {
            return INVOICENO;
        }

        public void setINVOICENO(String iNVOICENO) {
            INVOICENO = iNVOICENO;
        }

        public Long getINVOICEDATE() {
            return INVOICEDATE;
        }

        public void setINVOICEDATE(Long iNVOICEDATE) {
            INVOICEDATE = iNVOICEDATE;
        }

        public String getCUSTOMERNO() {
            return CUSTOMERNO;
        }

        public void setCUSTOMERNO(String cUSTOMERNO) {
            CUSTOMERNO = cUSTOMERNO;
        }

        @JsonView(View.Summary.class)
        private String INVOICENO;

        @JsonView(View.Summary.class)
        private Long INVOICEDATE;

        @JsonView(View.Summary.class)
        private String CUSTOMERNO;

    }

    public static class Record {

        @JsonView(View.Summary.class)
        private final String INVOICENO;

        @JsonView(View.Summary.class)
        private final String HEADERSEQNO;

        @JsonView(View.Summary.class)
        private final String MODULECODE;

        @JsonView(View.Summary.class)
        private final String INVOICETYPE;

        @JsonView(View.Summary.class)
        private final Date INVOICEDATE;

        @JsonView(View.Summary.class)
        private final Date TRANSACTIONDATE;

        @JsonView(View.Summary.class)
        private final String ARDIVISIONNO;

        @JsonView(View.Summary.class)
        private final String CUSTOMERNO;

        @JsonView(View.Summary.class)
        private final String TERMSCODE;

        @JsonView(View.Summary.class)
        private final String TAXSCHEDULE;

        @JsonView(View.Summary.class)
        private final String TAXEXEMPTNO;

        @JsonView(View.Summary.class)
        private final String SALESPERSONDIVISIONNO;

        @JsonView(View.Summary.class)
        private final String SALESPERSONNO;

        @JsonView(View.Summary.class)
        private final String CUSTOMERPONO;

        @JsonView(View.Summary.class)
        private final String APPLYTOINVOICENO;

        @JsonView(View.Summary.class)
        private final String COMMENT;

        @JsonView(View.Summary.class)
        private final String REPETITIVEINVOICEREFNO;

        @JsonView(View.Summary.class)
        private final String JOBNO;

        @JsonView(View.Summary.class)
        private final String INVOICEDUEDATE;

        @JsonView(View.Summary.class)
        private final String DISCOUNTDUEDATE;

        @JsonView(View.Summary.class)
        private final String SOURCEJOURNAL;

        @JsonView(View.Summary.class)
        private final String JOURNALNOGLBATCHNO;

        @JsonView(View.Summary.class)
        private final String BATCHFAX;

        @JsonView(View.Summary.class)
        private final String FAXNO;

        @JsonView(View.Summary.class)
        private final String SHIPPINGINVOICE;

        @JsonView(View.Summary.class)
        private final String SALESORDERNO;

        @JsonView(View.Summary.class)
        private final String ORDERTYPE;

        @JsonView(View.Summary.class)
        private final String ORDERDATE;

        @JsonView(View.Summary.class)
        private final String BILLTONAME;

        @JsonView(View.Summary.class)
        private final String BILLTOADDRESS1;

        @JsonView(View.Summary.class)
        private final String BILLTOADDRESS2;

        @JsonView(View.Summary.class)
        private final String BILLTOADDRESS3;

        @JsonView(View.Summary.class)
        private final String BILLTOCITY;

        @JsonView(View.Summary.class)
        private final String BILLTOSTATE;

        @JsonView(View.Summary.class)
        private final String BILLTOZIPCODE;

        @JsonView(View.Summary.class)
        private final String BILLTOCOUNTRYCODE;

        @JsonView(View.Summary.class)
        private final String SHIPTOCODE;

        @JsonView(View.Summary.class)
        private final String SHIPTONAME;

        @JsonView(View.Summary.class)
        private final String SHIPTOADDRESS1;

        @JsonView(View.Summary.class)
        private final String SHIPTOADDRESS2;

        @JsonView(View.Summary.class)
        private final String SHIPTOADDRESS3;

        @JsonView(View.Summary.class)
        private final String SHIPTOCITY;

        @JsonView(View.Summary.class)
        private final String SHIPTOSTATE;

        @JsonView(View.Summary.class)
        private final String SHIPTOZIPCODE;

        @JsonView(View.Summary.class)
        private final String SHIPTOCOUNTRYCODE;

        @JsonView(View.Summary.class)
        private final Date SHIPDATE;

        @JsonView(View.Summary.class)
        private final String SHIPVIA;

        @JsonView(View.Summary.class)
        private final String SHIPZONE;

        @JsonView(View.Summary.class)
        private final String FOB;

        @JsonView(View.Summary.class)
        private final String CONFIRMTO;

        @JsonView(View.Summary.class)
        private final String CHECKNOFORDEPOSIT;

        @JsonView(View.Summary.class)
        private final String SPLITCOMMISSIONS;

        @JsonView(View.Summary.class)
        private final String SALESPERSONDIVISIONNO2;

        @JsonView(View.Summary.class)
        private final String SALESPERSONNO2;

        @JsonView(View.Summary.class)
        private final String SALESPERSONDIVISIONNO3;

        @JsonView(View.Summary.class)
        private final String SALESPERSONNO3;

        @JsonView(View.Summary.class)
        private final String SALESPERSONDIVISIONNO4;

        @JsonView(View.Summary.class)
        private final String SALESPERSONNO4;

        @JsonView(View.Summary.class)
        private final String SALESPERSONDIVISIONNO5;

        @JsonView(View.Summary.class)
        private final String SALESPERSONNO5;

        @JsonView(View.Summary.class)
        private final String PAYMENTTYPE;

        @JsonView(View.Summary.class)
        private final String PAYMENTTYPECATEGORY;

        @JsonView(View.Summary.class)
        private final String OTHERPAYMENTTYPEREFNO;

        @JsonView(View.Summary.class)
        private final String RMANO;

        @JsonView(View.Summary.class)
        private final String EBMSUBMISSIONTYPE;

        @JsonView(View.Summary.class)
        private final String EBMUSERIDSUBMITTINGTHISORDER;

        @JsonView(View.Summary.class)
        private final String EBMUSERTYPE;

        @JsonView(View.Summary.class)
        private final String SHIPPERID;

        @JsonView(View.Summary.class)
        private final String USERKEY;

        @JsonView(View.Summary.class)
        private final String WAREHOUSECODE;

        @JsonView(View.Summary.class)
        private final String SHIPWEIGHT;

        @JsonView(View.Summary.class)
        private final String RESIDENTIALADDRESS;

        @JsonView(View.Summary.class)
        private final String EMAILADDRESS;

        @JsonView(View.Summary.class)
        private final String CRMUSERID;

        @JsonView(View.Summary.class)
        private final String CRMCOMPANYID;

        @JsonView(View.Summary.class)
        private final String CRMPERSONID;

        @JsonView(View.Summary.class)
        private final String CRMOPPORTUNITYID;

        @JsonView(View.Summary.class)
        private final Double TAXABLESALESAMT;

        @JsonView(View.Summary.class)
        private final Double NONTAXABLESALESAMT;

        @JsonView(View.Summary.class)
        private final Double FREIGHTAMT;

        @JsonView(View.Summary.class)
        private final Double SALESTAXAMT;

        @JsonView(View.Summary.class)
        private final Double COSTOFSALESAMT;

        @JsonView(View.Summary.class)
        private final Double AMOUNTSUBJECTTODISCOUNT;

        @JsonView(View.Summary.class)
        private final Double DISCOUNTRATE;

        @JsonView(View.Summary.class)
        private final Double DISCOUNTAMT;

        @JsonView(View.Summary.class)
        private final Double SALESSUBJECTTOCOMM;

        @JsonView(View.Summary.class)
        private final Double COSTSUBJECTTOCOMM;

        @JsonView(View.Summary.class)
        private final Double COMMISSIONRATE;

        @JsonView(View.Summary.class)
        private final Double COMMISSIONAMT;

        @JsonView(View.Summary.class)
        private final Double SPLITCOMMRATE2;

        @JsonView(View.Summary.class)
        private final Double SPLITCOMMRATE3;

        @JsonView(View.Summary.class)
        private final Double SPLITCOMMRATE4;

        @JsonView(View.Summary.class)
        private final Double SPLITCOMMRATE5;

        @JsonView(View.Summary.class)
        private final Double DEPOSITAMT;

        @JsonView(View.Summary.class)
        private final Double WEIGHT;

        @JsonView(View.Summary.class)
        private final Double RETENTIONAMT;

        @JsonView(View.Summary.class)
        private final Double NUMBEROFPACKAGES;

        @JsonView(View.Summary.class)
        private final Date DATEUPDATED;

        @JsonView(View.Summary.class)
        private final String TIMEUPDATED;

        @JsonView(View.Summary.class)
        private final String USERUPDATEDKEY;

        @JsonView(View.Summary.class)
        private final String ARMC_234_ENTRYCURRENCY;

        @JsonView(View.Summary.class)
        private final Double ARMC_234_ENTRYRATE;

        @JsonView(View.Summary.class)
        private final Double ARMC_234_BASECOSTOFSALESAMT;

        @JsonView(View.Summary.class)
        private final Double ARMC_234_BASECOSTSUBJECTTOCOMM;

        @JsonView(View.Summary.class)
        private final Double ARMC_234_BASECOMMISSIONAMT;

        @JsonView(View.Summary.class)
        private final String BILLTODIVISIONNO;

        @JsonView(View.Summary.class)
        private final String BILLTOCUSTOMERNO;

        public Record(String INVOICENO, String HEADERSEQNO, String MODULECODE, String INVOICETYPE, Date INVOICEDATE,
                Date TRANSACTIONDATE, String ARDIVISIONNO, String CUSTOMERNO, String TERMSCODE, String TAXSCHEDULE,
                String TAXEXEMPTNO, String SALESPERSONDIVISIONNO, String SALESPERSONNO, String CUSTOMERPONO,
                String APPLYTOINVOICENO, String COMMENT, String REPETITIVEINVOICEREFNO, String JOBNO,
                String INVOICEDUEDATE, String DISCOUNTDUEDATE, String SOURCEJOURNAL, String JOURNALNOGLBATCHNO,
                String BATCHFAX, String FAXNO, String SHIPPINGINVOICE, String SALESORDERNO, String ORDERTYPE,
                String ORDERDATE, String BILLTONAME, String BILLTOADDRESS1, String BILLTOADDRESS2,
                String BILLTOADDRESS3, String BILLTOCITY, String BILLTOSTATE, String BILLTOZIPCODE,
                String BILLTOCOUNTRYCODE, String SHIPTOCODE, String SHIPTONAME, String SHIPTOADDRESS1,
                String SHIPTOADDRESS2, String SHIPTOADDRESS3, String SHIPTOCITY, String SHIPTOSTATE,
                String SHIPTOZIPCODE, String SHIPTOCOUNTRYCODE, Date SHIPDATE, String SHIPVIA, String SHIPZONE,
                String FOB, String CONFIRMTO, String CHECKNOFORDEPOSIT, String SPLITCOMMISSIONS,
                String SALESPERSONDIVISIONNO2, String SALESPERSONNO2, String SALESPERSONDIVISIONNO3,
                String SALESPERSONNO3, String SALESPERSONDIVISIONNO4, String SALESPERSONNO4,
                String SALESPERSONDIVISIONNO5, String SALESPERSONNO5, String PAYMENTTYPE, String PAYMENTTYPECATEGORY,
                String OTHERPAYMENTTYPEREFNO, String RMANO, String EBMSUBMISSIONTYPE,
                String EBMUSERIDSUBMITTINGTHISORDER, String EBMUSERTYPE, String SHIPPERID, String USERKEY,
                String WAREHOUSECODE, String SHIPWEIGHT, String RESIDENTIALADDRESS, String EMAILADDRESS,
                String CRMUSERID, String CRMCOMPANYID, String CRMPERSONID, String CRMOPPORTUNITYID,
                Double TAXABLESALESAMT, Double NONTAXABLESALESAMT, Double FREIGHTAMT, Double SALESTAXAMT,
                Double COSTOFSALESAMT, Double AMOUNTSUBJECTTODISCOUNT, Double DISCOUNTRATE, Double DISCOUNTAMT,
                Double SALESSUBJECTTOCOMM, Double COSTSUBJECTTOCOMM, Double COMMISSIONRATE, Double COMMISSIONAMT,
                Double SPLITCOMMRATE2, Double SPLITCOMMRATE3, Double SPLITCOMMRATE4, Double SPLITCOMMRATE5,
                Double DEPOSITAMT, Double WEIGHT, Double RETENTIONAMT, Double NUMBEROFPACKAGES, Date DATEUPDATED,
                String TIMEUPDATED, String USERUPDATEDKEY, String ARMC_234_ENTRYCURRENCY, Double ARMC_234_ENTRYRATE,
                Double ARMC_234_BASECOSTOFSALESAMT, Double ARMC_234_BASECOSTSUBJECTTOCOMM,
                Double ARMC_234_BASECOMMISSIONAMT, String BILLTODIVISIONNO, String BILLTOCUSTOMERNO) {
            this.INVOICENO = INVOICENO;
            this.HEADERSEQNO = HEADERSEQNO;
            this.MODULECODE = MODULECODE;
            this.INVOICETYPE = INVOICETYPE;
            this.INVOICEDATE = INVOICEDATE;
            this.TRANSACTIONDATE = TRANSACTIONDATE;
            this.ARDIVISIONNO = ARDIVISIONNO;
            this.CUSTOMERNO = CUSTOMERNO;
            this.TERMSCODE = TERMSCODE;
            this.TAXSCHEDULE = TAXSCHEDULE;
            this.TAXEXEMPTNO = TAXEXEMPTNO;
            this.SALESPERSONDIVISIONNO = SALESPERSONDIVISIONNO;
            this.SALESPERSONNO = SALESPERSONNO;
            this.CUSTOMERPONO = CUSTOMERPONO;
            this.APPLYTOINVOICENO = APPLYTOINVOICENO;
            this.COMMENT = COMMENT;
            this.REPETITIVEINVOICEREFNO = REPETITIVEINVOICEREFNO;
            this.JOBNO = JOBNO;
            this.INVOICEDUEDATE = INVOICEDUEDATE;
            this.DISCOUNTDUEDATE = DISCOUNTDUEDATE;
            this.SOURCEJOURNAL = SOURCEJOURNAL;
            this.JOURNALNOGLBATCHNO = JOURNALNOGLBATCHNO;
            this.BATCHFAX = BATCHFAX;
            this.FAXNO = FAXNO;
            this.SHIPPINGINVOICE = SHIPPINGINVOICE;
            this.SALESORDERNO = SALESORDERNO;
            this.ORDERTYPE = ORDERTYPE;
            this.ORDERDATE = ORDERDATE;
            this.BILLTONAME = BILLTONAME;
            this.BILLTOADDRESS1 = BILLTOADDRESS1;
            this.BILLTOADDRESS2 = BILLTOADDRESS2;
            this.BILLTOADDRESS3 = BILLTOADDRESS3;
            this.BILLTOCITY = BILLTOCITY;
            this.BILLTOSTATE = BILLTOSTATE;
            this.BILLTOZIPCODE = BILLTOZIPCODE;
            this.BILLTOCOUNTRYCODE = BILLTOCOUNTRYCODE;
            this.SHIPTOCODE = SHIPTOCODE;
            this.SHIPTONAME = SHIPTONAME;
            this.SHIPTOADDRESS1 = SHIPTOADDRESS1;
            this.SHIPTOADDRESS2 = SHIPTOADDRESS2;
            this.SHIPTOADDRESS3 = SHIPTOADDRESS3;
            this.SHIPTOCITY = SHIPTOCITY;
            this.SHIPTOSTATE = SHIPTOSTATE;
            this.SHIPTOZIPCODE = SHIPTOZIPCODE;
            this.SHIPTOCOUNTRYCODE = SHIPTOCOUNTRYCODE;
            this.SHIPDATE = SHIPDATE;
            this.SHIPVIA = SHIPVIA;
            this.SHIPZONE = SHIPZONE;
            this.FOB = FOB;
            this.CONFIRMTO = CONFIRMTO;
            this.CHECKNOFORDEPOSIT = CHECKNOFORDEPOSIT;
            this.SPLITCOMMISSIONS = SPLITCOMMISSIONS;
            this.SALESPERSONDIVISIONNO2 = SALESPERSONDIVISIONNO2;
            this.SALESPERSONNO2 = SALESPERSONNO2;
            this.SALESPERSONDIVISIONNO3 = SALESPERSONDIVISIONNO3;
            this.SALESPERSONNO3 = SALESPERSONNO3;
            this.SALESPERSONDIVISIONNO4 = SALESPERSONDIVISIONNO4;
            this.SALESPERSONNO4 = SALESPERSONNO4;
            this.SALESPERSONDIVISIONNO5 = SALESPERSONDIVISIONNO5;
            this.SALESPERSONNO5 = SALESPERSONNO5;
            this.PAYMENTTYPE = PAYMENTTYPE;
            this.PAYMENTTYPECATEGORY = PAYMENTTYPECATEGORY;
            this.OTHERPAYMENTTYPEREFNO = OTHERPAYMENTTYPEREFNO;
            this.RMANO = RMANO;
            this.EBMSUBMISSIONTYPE = EBMSUBMISSIONTYPE;
            this.EBMUSERIDSUBMITTINGTHISORDER = EBMUSERIDSUBMITTINGTHISORDER;
            this.EBMUSERTYPE = EBMUSERTYPE;
            this.SHIPPERID = SHIPPERID;
            this.USERKEY = USERKEY;
            this.WAREHOUSECODE = WAREHOUSECODE;
            this.SHIPWEIGHT = SHIPWEIGHT;
            this.RESIDENTIALADDRESS = RESIDENTIALADDRESS;
            this.EMAILADDRESS = EMAILADDRESS;
            this.CRMUSERID = CRMUSERID;
            this.CRMCOMPANYID = CRMCOMPANYID;
            this.CRMPERSONID = CRMPERSONID;
            this.CRMOPPORTUNITYID = CRMOPPORTUNITYID;
            this.TAXABLESALESAMT = TAXABLESALESAMT;
            this.NONTAXABLESALESAMT = NONTAXABLESALESAMT;
            this.FREIGHTAMT = FREIGHTAMT;
            this.SALESTAXAMT = SALESTAXAMT;
            this.COSTOFSALESAMT = COSTOFSALESAMT;
            this.AMOUNTSUBJECTTODISCOUNT = AMOUNTSUBJECTTODISCOUNT;
            this.DISCOUNTRATE = DISCOUNTRATE;
            this.DISCOUNTAMT = DISCOUNTAMT;
            this.SALESSUBJECTTOCOMM = SALESSUBJECTTOCOMM;
            this.COSTSUBJECTTOCOMM = COSTSUBJECTTOCOMM;
            this.COMMISSIONRATE = COMMISSIONRATE;
            this.COMMISSIONAMT = COMMISSIONAMT;
            this.SPLITCOMMRATE2 = SPLITCOMMRATE2;
            this.SPLITCOMMRATE3 = SPLITCOMMRATE3;
            this.SPLITCOMMRATE4 = SPLITCOMMRATE4;
            this.SPLITCOMMRATE5 = SPLITCOMMRATE5;
            this.DEPOSITAMT = DEPOSITAMT;
            this.WEIGHT = WEIGHT;
            this.RETENTIONAMT = RETENTIONAMT;
            this.NUMBEROFPACKAGES = NUMBEROFPACKAGES;
            this.DATEUPDATED = DATEUPDATED;
            this.TIMEUPDATED = TIMEUPDATED;
            this.USERUPDATEDKEY = USERUPDATEDKEY;
            this.ARMC_234_ENTRYCURRENCY = ARMC_234_ENTRYCURRENCY;
            this.ARMC_234_ENTRYRATE = ARMC_234_ENTRYRATE;
            this.ARMC_234_BASECOSTOFSALESAMT = ARMC_234_BASECOSTOFSALESAMT;
            this.ARMC_234_BASECOSTSUBJECTTOCOMM = ARMC_234_BASECOSTSUBJECTTOCOMM;
            this.ARMC_234_BASECOMMISSIONAMT = ARMC_234_BASECOMMISSIONAMT;
            this.BILLTODIVISIONNO = BILLTODIVISIONNO;
            this.BILLTOCUSTOMERNO = BILLTOCUSTOMERNO;
        }

        public String getINVOICENO() {
            return INVOICENO;
        }

        public String getHEADERSEQNO() {
            return HEADERSEQNO;
        }

        public String getMODULECODE() {
            return MODULECODE;
        }

        public String getINVOICETYPE() {
            return INVOICETYPE;
        }

        public Date getINVOICEDATE() {
            return INVOICEDATE;
        }

        public Date getTRANSACTIONDATE() {
            return TRANSACTIONDATE;
        }

        public String getARDIVISIONNO() {
            return ARDIVISIONNO;
        }

        public String getCUSTOMERNO() {
            return CUSTOMERNO;
        }

        public String getTERMSCODE() {
            return TERMSCODE;
        }

        public String getTAXSCHEDULE() {
            return TAXSCHEDULE;
        }

        public String getTAXEXEMPTNO() {
            return TAXEXEMPTNO;
        }

        public String getSALESPERSONDIVISIONNO() {
            return SALESPERSONDIVISIONNO;
        }

        public String getSALESPERSONNO() {
            return SALESPERSONNO;
        }

        public String getCUSTOMERPONO() {
            return CUSTOMERPONO;
        }

        public String getAPPLYTOINVOICENO() {
            return APPLYTOINVOICENO;
        }

        public String getCOMMENT() {
            return COMMENT;
        }

        public String getREPETITIVEINVOICEREFNO() {
            return REPETITIVEINVOICEREFNO;
        }

        public String getJOBNO() {
            return JOBNO;
        }

        public String getINVOICEDUEDATE() {
            return INVOICEDUEDATE;
        }

        public String getDISCOUNTDUEDATE() {
            return DISCOUNTDUEDATE;
        }

        public String getSOURCEJOURNAL() {
            return SOURCEJOURNAL;
        }

        public String getJOURNALNOGLBATCHNO() {
            return JOURNALNOGLBATCHNO;
        }

        public String getBATCHFAX() {
            return BATCHFAX;
        }

        public String getFAXNO() {
            return FAXNO;
        }

        public String getSHIPPINGINVOICE() {
            return SHIPPINGINVOICE;
        }

        public String getSALESORDERNO() {
            return SALESORDERNO;
        }

        public String getORDERTYPE() {
            return ORDERTYPE;
        }

        public String getORDERDATE() {
            return ORDERDATE;
        }

        public String getBILLTONAME() {
            return BILLTONAME;
        }

        public String getBILLTOADDRESS1() {
            return BILLTOADDRESS1;
        }

        public String getBILLTOADDRESS2() {
            return BILLTOADDRESS2;
        }

        public String getBILLTOADDRESS3() {
            return BILLTOADDRESS3;
        }

        public String getBILLTOCITY() {
            return BILLTOCITY;
        }

        public String getBILLTOSTATE() {
            return BILLTOSTATE;
        }

        public String getBILLTOZIPCODE() {
            return BILLTOZIPCODE;
        }

        public String getBILLTOCOUNTRYCODE() {
            return BILLTOCOUNTRYCODE;
        }

        public String getSHIPTOCODE() {
            return SHIPTOCODE;
        }

        public String getSHIPTONAME() {
            return SHIPTONAME;
        }

        public String getSHIPTOADDRESS1() {
            return SHIPTOADDRESS1;
        }

        public String getSHIPTOADDRESS2() {
            return SHIPTOADDRESS2;
        }

        public String getSHIPTOADDRESS3() {
            return SHIPTOADDRESS3;
        }

        public String getSHIPTOCITY() {
            return SHIPTOCITY;
        }

        public String getSHIPTOSTATE() {
            return SHIPTOSTATE;
        }

        public String getSHIPTOZIPCODE() {
            return SHIPTOZIPCODE;
        }

        public String getSHIPTOCOUNTRYCODE() {
            return SHIPTOCOUNTRYCODE;
        }

        public Date getSHIPDATE() {
            return SHIPDATE;
        }

        public String getSHIPVIA() {
            return SHIPVIA;
        }

        public String getSHIPZONE() {
            return SHIPZONE;
        }

        public String getFOB() {
            return FOB;
        }

        public String getCONFIRMTO() {
            return CONFIRMTO;
        }

        public String getCHECKNOFORDEPOSIT() {
            return CHECKNOFORDEPOSIT;
        }

        public String getSPLITCOMMISSIONS() {
            return SPLITCOMMISSIONS;
        }

        public String getSALESPERSONDIVISIONNO2() {
            return SALESPERSONDIVISIONNO2;
        }

        public String getSALESPERSONNO2() {
            return SALESPERSONNO2;
        }

        public String getSALESPERSONDIVISIONNO3() {
            return SALESPERSONDIVISIONNO3;
        }

        public String getSALESPERSONNO3() {
            return SALESPERSONNO3;
        }

        public String getSALESPERSONDIVISIONNO4() {
            return SALESPERSONDIVISIONNO4;
        }

        public String getSALESPERSONNO4() {
            return SALESPERSONNO4;
        }

        public String getSALESPERSONDIVISIONNO5() {
            return SALESPERSONDIVISIONNO5;
        }

        public String getSALESPERSONNO5() {
            return SALESPERSONNO5;
        }

        public String getPAYMENTTYPE() {
            return PAYMENTTYPE;
        }

        public String getPAYMENTTYPECATEGORY() {
            return PAYMENTTYPECATEGORY;
        }

        public String getOTHERPAYMENTTYPEREFNO() {
            return OTHERPAYMENTTYPEREFNO;
        }

        public String getRMANO() {
            return RMANO;
        }

        public String getEBMSUBMISSIONTYPE() {
            return EBMSUBMISSIONTYPE;
        }

        public String getEBMUSERIDSUBMITTINGTHISORDER() {
            return EBMUSERIDSUBMITTINGTHISORDER;
        }

        public String getEBMUSERTYPE() {
            return EBMUSERTYPE;
        }

        public String getSHIPPERID() {
            return SHIPPERID;
        }

        public String getUSERKEY() {
            return USERKEY;
        }

        public String getWAREHOUSECODE() {
            return WAREHOUSECODE;
        }

        public String getSHIPWEIGHT() {
            return SHIPWEIGHT;
        }

        public String getRESIDENTIALADDRESS() {
            return RESIDENTIALADDRESS;
        }

        public String getEMAILADDRESS() {
            return EMAILADDRESS;
        }

        public String getCRMUSERID() {
            return CRMUSERID;
        }

        public String getCRMCOMPANYID() {
            return CRMCOMPANYID;
        }

        public String getCRMPERSONID() {
            return CRMPERSONID;
        }

        public String getCRMOPPORTUNITYID() {
            return CRMOPPORTUNITYID;
        }

        public Double getTAXABLESALESAMT() {
            return TAXABLESALESAMT;
        }

        public Double getNONTAXABLESALESAMT() {
            return NONTAXABLESALESAMT;
        }

        public Double getFREIGHTAMT() {
            return FREIGHTAMT;
        }

        public Double getSALESTAXAMT() {
            return SALESTAXAMT;
        }

        public Double getCOSTOFSALESAMT() {
            return COSTOFSALESAMT;
        }

        public Double getAMOUNTSUBJECTTODISCOUNT() {
            return AMOUNTSUBJECTTODISCOUNT;
        }

        public Double getDISCOUNTRATE() {
            return DISCOUNTRATE;
        }

        public Double getDISCOUNTAMT() {
            return DISCOUNTAMT;
        }

        public Double getSALESSUBJECTTOCOMM() {
            return SALESSUBJECTTOCOMM;
        }

        public Double getCOSTSUBJECTTOCOMM() {
            return COSTSUBJECTTOCOMM;
        }

        public Double getCOMMISSIONRATE() {
            return COMMISSIONRATE;
        }

        public Double getCOMMISSIONAMT() {
            return COMMISSIONAMT;
        }

        public Double getSPLITCOMMRATE2() {
            return SPLITCOMMRATE2;
        }

        public Double getSPLITCOMMRATE3() {
            return SPLITCOMMRATE3;
        }

        public Double getSPLITCOMMRATE4() {
            return SPLITCOMMRATE4;
        }

        public Double getSPLITCOMMRATE5() {
            return SPLITCOMMRATE5;
        }

        public Double getDEPOSITAMT() {
            return DEPOSITAMT;
        }

        public Double getWEIGHT() {
            return WEIGHT;
        }

        public Double getRETENTIONAMT() {
            return RETENTIONAMT;
        }

        public Double getNUMBEROFPACKAGES() {
            return NUMBEROFPACKAGES;
        }

        public Date getDATEUPDATED() {
            return DATEUPDATED;
        }

        public String getTIMEUPDATED() {
            return TIMEUPDATED;
        }

        public String getUSERUPDATEDKEY() {
            return USERUPDATEDKEY;
        }

        public String getARMC_234_ENTRYCURRENCY() {
            return ARMC_234_ENTRYCURRENCY;
        }

        public Double getARMC_234_ENTRYRATE() {
            return ARMC_234_ENTRYRATE;
        }

        public Double getARMC_234_BASECOSTOFSALESAMT() {
            return ARMC_234_BASECOSTOFSALESAMT;
        }

        public Double getARMC_234_BASECOSTSUBJECTTOCOMM() {
            return ARMC_234_BASECOSTSUBJECTTOCOMM;
        }

        public Double getARMC_234_BASECOMMISSIONAMT() {
            return ARMC_234_BASECOMMISSIONAMT;
        }

        public String getBILLTODIVISIONNO() {
            return BILLTODIVISIONNO;
        }

        public String getBILLTOCUSTOMERNO() {
            return BILLTOCUSTOMERNO;
        }

    }

    @JsonView(View.Summary.class)
    private final Key key;

    @JsonView(View.Summary.class)
    private final List<Record> records;

    public ArInvoiceHistoryHeaderDto(Key key, List<Record> records) {
        super();
        this.key = key;
        this.records = records;
    }

    public Key getKey() {
        return key;
    }

    public List<Record> getRecords() {
        return records;
    }

}
