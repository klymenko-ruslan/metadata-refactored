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

        @JsonView(View.Summary.class)
        private final String INVOICENO;

        @JsonView(View.Summary.class)
        private final Date INVOICEDATE;

        @JsonView(View.Summary.class)
        private final String CUSTOMERNO;

        public Key(String iNVOICENO, Date iNVOICEDATE, String cUSTOMERNO) {
            super();
            INVOICENO = iNVOICENO;
            INVOICEDATE = iNVOICEDATE;
            CUSTOMERNO = cUSTOMERNO;
        }

        public String getINVOICENO() {
            return INVOICENO;
        }

        public Date getINVOICEDATE() {
            return INVOICEDATE;
        }

        public String getCUSTOMERNO() {
            return CUSTOMERNO;
        }

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

        public Record(String iNVOICENO, String hEADERSEQNO, String mODULECODE, String iNVOICETYPE, Date iNVOICEDATE,
                Date tRANSACTIONDATE, String aRDIVISIONNO, String cUSTOMERNO, String tERMSCODE, String tAXSCHEDULE,
                String tAXEXEMPTNO, String sALESPERSONDIVISIONNO, String sALESPERSONNO, String cUSTOMERPONO,
                String aPPLYTOINVOICENO, String cOMMENT, String rEPETITIVEINVOICEREFNO, String jOBNO,
                String iNVOICEDUEDATE, String dISCOUNTDUEDATE, String sOURCEJOURNAL, String jOURNALNOGLBATCHNO,
                String bATCHFAX, String fAXNO, String sHIPPINGINVOICE, String sALESORDERNO, String oRDERTYPE,
                String oRDERDATE, String bILLTONAME, String bILLTOADDRESS1, String bILLTOADDRESS2,
                String bILLTOADDRESS3, String bILLTOCITY, String bILLTOSTATE, String bILLTOZIPCODE,
                String bILLTOCOUNTRYCODE, String sHIPTOCODE, String sHIPTONAME, String sHIPTOADDRESS1,
                String sHIPTOADDRESS2, String sHIPTOADDRESS3, String sHIPTOCITY, String sHIPTOSTATE,
                String sHIPTOZIPCODE, String sHIPTOCOUNTRYCODE, Date sHIPDATE, String sHIPVIA, String sHIPZONE,
                String fOB, String cONFIRMTO, String cHECKNOFORDEPOSIT, String sPLITCOMMISSIONS,
                String sALESPERSONDIVISIONNO2, String sALESPERSONNO2, String sALESPERSONDIVISIONNO3,
                String sALESPERSONNO3, String sALESPERSONDIVISIONNO4, String sALESPERSONNO4,
                String sALESPERSONDIVISIONNO5, String sALESPERSONNO5, String pAYMENTTYPE, String pAYMENTTYPECATEGORY,
                String oTHERPAYMENTTYPEREFNO, String rMANO, String eBMSUBMISSIONTYPE,
                String eBMUSERIDSUBMITTINGTHISORDER, String eBMUSERTYPE, String sHIPPERID, String uSERKEY,
                String wAREHOUSECODE, String sHIPWEIGHT, String rESIDENTIALADDRESS, String eMAILADDRESS,
                String cRMUSERID, String cRMCOMPANYID, String cRMPERSONID, String cRMOPPORTUNITYID,
                Double tAXABLESALESAMT, Double nONTAXABLESALESAMT, Double fREIGHTAMT, Double sALESTAXAMT,
                Double cOSTOFSALESAMT, Double aMOUNTSUBJECTTODISCOUNT, Double dISCOUNTRATE, Double dISCOUNTAMT,
                Double sALESSUBJECTTOCOMM, Double cOSTSUBJECTTOCOMM, Double cOMMISSIONRATE, Double cOMMISSIONAMT,
                Double sPLITCOMMRATE2, Double sPLITCOMMRATE3, Double sPLITCOMMRATE4, Double sPLITCOMMRATE5,
                Double dEPOSITAMT, Double wEIGHT, Double rETENTIONAMT, Double nUMBEROFPACKAGES, Date dATEUPDATED,
                String tIMEUPDATED, String uSERUPDATEDKEY, String aRMC_234_ENTRYCURRENCY, Double aRMC_234_ENTRYRATE,
                Double aRMC_234_BASECOSTOFSALESAMT, Double aRMC_234_BASECOSTSUBJECTTOCOMM,
                Double aRMC_234_BASECOMMISSIONAMT, String bILLTODIVISIONNO, String bILLTOCUSTOMERNO) {
            INVOICENO = iNVOICENO;
            HEADERSEQNO = hEADERSEQNO;
            MODULECODE = mODULECODE;
            INVOICETYPE = iNVOICETYPE;
            INVOICEDATE = iNVOICEDATE;
            TRANSACTIONDATE = tRANSACTIONDATE;
            ARDIVISIONNO = aRDIVISIONNO;
            CUSTOMERNO = cUSTOMERNO;
            TERMSCODE = tERMSCODE;
            TAXSCHEDULE = tAXSCHEDULE;
            TAXEXEMPTNO = tAXEXEMPTNO;
            SALESPERSONDIVISIONNO = sALESPERSONDIVISIONNO;
            SALESPERSONNO = sALESPERSONNO;
            CUSTOMERPONO = cUSTOMERPONO;
            APPLYTOINVOICENO = aPPLYTOINVOICENO;
            COMMENT = cOMMENT;
            REPETITIVEINVOICEREFNO = rEPETITIVEINVOICEREFNO;
            JOBNO = jOBNO;
            INVOICEDUEDATE = iNVOICEDUEDATE;
            DISCOUNTDUEDATE = dISCOUNTDUEDATE;
            SOURCEJOURNAL = sOURCEJOURNAL;
            JOURNALNOGLBATCHNO = jOURNALNOGLBATCHNO;
            BATCHFAX = bATCHFAX;
            FAXNO = fAXNO;
            SHIPPINGINVOICE = sHIPPINGINVOICE;
            SALESORDERNO = sALESORDERNO;
            ORDERTYPE = oRDERTYPE;
            ORDERDATE = oRDERDATE;
            BILLTONAME = bILLTONAME;
            BILLTOADDRESS1 = bILLTOADDRESS1;
            BILLTOADDRESS2 = bILLTOADDRESS2;
            BILLTOADDRESS3 = bILLTOADDRESS3;
            BILLTOCITY = bILLTOCITY;
            BILLTOSTATE = bILLTOSTATE;
            BILLTOZIPCODE = bILLTOZIPCODE;
            BILLTOCOUNTRYCODE = bILLTOCOUNTRYCODE;
            SHIPTOCODE = sHIPTOCODE;
            SHIPTONAME = sHIPTONAME;
            SHIPTOADDRESS1 = sHIPTOADDRESS1;
            SHIPTOADDRESS2 = sHIPTOADDRESS2;
            SHIPTOADDRESS3 = sHIPTOADDRESS3;
            SHIPTOCITY = sHIPTOCITY;
            SHIPTOSTATE = sHIPTOSTATE;
            SHIPTOZIPCODE = sHIPTOZIPCODE;
            SHIPTOCOUNTRYCODE = sHIPTOCOUNTRYCODE;
            SHIPDATE = sHIPDATE;
            SHIPVIA = sHIPVIA;
            SHIPZONE = sHIPZONE;
            FOB = fOB;
            CONFIRMTO = cONFIRMTO;
            CHECKNOFORDEPOSIT = cHECKNOFORDEPOSIT;
            SPLITCOMMISSIONS = sPLITCOMMISSIONS;
            SALESPERSONDIVISIONNO2 = sALESPERSONDIVISIONNO2;
            SALESPERSONNO2 = sALESPERSONNO2;
            SALESPERSONDIVISIONNO3 = sALESPERSONDIVISIONNO3;
            SALESPERSONNO3 = sALESPERSONNO3;
            SALESPERSONDIVISIONNO4 = sALESPERSONDIVISIONNO4;
            SALESPERSONNO4 = sALESPERSONNO4;
            SALESPERSONDIVISIONNO5 = sALESPERSONDIVISIONNO5;
            SALESPERSONNO5 = sALESPERSONNO5;
            PAYMENTTYPE = pAYMENTTYPE;
            PAYMENTTYPECATEGORY = pAYMENTTYPECATEGORY;
            OTHERPAYMENTTYPEREFNO = oTHERPAYMENTTYPEREFNO;
            RMANO = rMANO;
            EBMSUBMISSIONTYPE = eBMSUBMISSIONTYPE;
            EBMUSERIDSUBMITTINGTHISORDER = eBMUSERIDSUBMITTINGTHISORDER;
            EBMUSERTYPE = eBMUSERTYPE;
            SHIPPERID = sHIPPERID;
            USERKEY = uSERKEY;
            WAREHOUSECODE = wAREHOUSECODE;
            SHIPWEIGHT = sHIPWEIGHT;
            RESIDENTIALADDRESS = rESIDENTIALADDRESS;
            EMAILADDRESS = eMAILADDRESS;
            CRMUSERID = cRMUSERID;
            CRMCOMPANYID = cRMCOMPANYID;
            CRMPERSONID = cRMPERSONID;
            CRMOPPORTUNITYID = cRMOPPORTUNITYID;
            TAXABLESALESAMT = tAXABLESALESAMT;
            NONTAXABLESALESAMT = nONTAXABLESALESAMT;
            FREIGHTAMT = fREIGHTAMT;
            SALESTAXAMT = sALESTAXAMT;
            COSTOFSALESAMT = cOSTOFSALESAMT;
            AMOUNTSUBJECTTODISCOUNT = aMOUNTSUBJECTTODISCOUNT;
            DISCOUNTRATE = dISCOUNTRATE;
            DISCOUNTAMT = dISCOUNTAMT;
            SALESSUBJECTTOCOMM = sALESSUBJECTTOCOMM;
            COSTSUBJECTTOCOMM = cOSTSUBJECTTOCOMM;
            COMMISSIONRATE = cOMMISSIONRATE;
            COMMISSIONAMT = cOMMISSIONAMT;
            SPLITCOMMRATE2 = sPLITCOMMRATE2;
            SPLITCOMMRATE3 = sPLITCOMMRATE3;
            SPLITCOMMRATE4 = sPLITCOMMRATE4;
            SPLITCOMMRATE5 = sPLITCOMMRATE5;
            DEPOSITAMT = dEPOSITAMT;
            WEIGHT = wEIGHT;
            RETENTIONAMT = rETENTIONAMT;
            NUMBEROFPACKAGES = nUMBEROFPACKAGES;
            DATEUPDATED = dATEUPDATED;
            TIMEUPDATED = tIMEUPDATED;
            USERUPDATEDKEY = uSERUPDATEDKEY;
            ARMC_234_ENTRYCURRENCY = aRMC_234_ENTRYCURRENCY;
            ARMC_234_ENTRYRATE = aRMC_234_ENTRYRATE;
            ARMC_234_BASECOSTOFSALESAMT = aRMC_234_BASECOSTOFSALESAMT;
            ARMC_234_BASECOSTSUBJECTTOCOMM = aRMC_234_BASECOSTSUBJECTTOCOMM;
            ARMC_234_BASECOMMISSIONAMT = aRMC_234_BASECOMMISSIONAMT;
            BILLTODIVISIONNO = bILLTODIVISIONNO;
            BILLTOCUSTOMERNO = bILLTOCUSTOMERNO;
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
