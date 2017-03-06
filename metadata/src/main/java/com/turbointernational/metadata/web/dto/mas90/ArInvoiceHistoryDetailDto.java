/**
 * DTO for the table AR_INVOICEHISTORYDETAIL in the MsSql server.
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
public class ArInvoiceHistoryDetailDto {

    @JsonInclude(ALWAYS)
    public static class Key {

        @JsonView(View.Summary.class)
        private String INVOICENO;

        @JsonView(View.Summary.class)
        private String ITEMCODE;

        public Key() {
        }

        public String getINVOICENO() {
            return INVOICENO;
        }

        public void setINVOICENO(String iNVOICENO) {
            INVOICENO = iNVOICENO;
        }

        public String getITEMCODE() {
            return ITEMCODE;
        }

        public void setITEMCODE(String iTEMCODE) {
            ITEMCODE = iTEMCODE;
        }

    }

    @JsonInclude(NON_NULL)
    public static class Record {

        @JsonView(View.Summary.class)
        private final String INVOICENO;

        @JsonView(View.Summary.class)
        private final String HEADERSEQNO;

        @JsonView(View.Summary.class)
        private final String DETAILSEQNO;

        @JsonView(View.Summary.class)
        private final String ITEMCODE;

        @JsonView(View.Summary.class)
        private final String ITEMTYPE;

        @JsonView(View.Summary.class)
        private final String ITEMCODEDESC;

        @JsonView(View.Summary.class)
        private final String EXTENDEDDESCRIPTIONKEY;

        @JsonView(View.Summary.class)
        private final String SALESACCTKEY;

        @JsonView(View.Summary.class)
        private final String COSTOFGOODSSOLDACCTKEY;

        @JsonView(View.Summary.class)
        private final String INVENTORYACCTKEY;

        @JsonView(View.Summary.class)
        private final String UNITOFMEASURE;

        @JsonView(View.Summary.class)
        private final String SUBJECTTOEXEMPTION;

        @JsonView(View.Summary.class)
        private final String COMMISSIONABLE;

        @JsonView(View.Summary.class)
        private final String TAXCLASS;

        @JsonView(View.Summary.class)
        private final String DISCOUNT;

        @JsonView(View.Summary.class)
        private final String DROPSHIP;

        @JsonView(View.Summary.class)
        private final String WAREHOUSECODE;

        @JsonView(View.Summary.class)
        private final String PRICELEVEL;

        @JsonView(View.Summary.class)
        private final String PRODUCTLINE;

        @JsonView(View.Summary.class)
        private final String VALUATION;

        @JsonView(View.Summary.class)
        private final String PRICEOVERRIDDEN;

        @JsonView(View.Summary.class)
        private final String ORDERWAREHOUSE;

        @JsonView(View.Summary.class)
        private final String REVISION;

        @JsonView(View.Summary.class)
        private final String BILLOPTION1;

        @JsonView(View.Summary.class)
        private final String BILLOPTION2;

        @JsonView(View.Summary.class)
        private final String BILLOPTION3;

        @JsonView(View.Summary.class)
        private final String BILLOPTION4;

        @JsonView(View.Summary.class)
        private final String BILLOPTION5;

        @JsonView(View.Summary.class)
        private final String BILLOPTION6;

        @JsonView(View.Summary.class)
        private final String BILLOPTION7;

        @JsonView(View.Summary.class)
        private final String BILLOPTION8;

        @JsonView(View.Summary.class)
        private final String BILLOPTION9;

        @JsonView(View.Summary.class)
        private final String KITITEM;

        @JsonView(View.Summary.class)
        private final String EXPLODEDKITITEM;

        @JsonView(View.Summary.class)
        private final String SKIPPRINTCOMPLINE;

        @JsonView(View.Summary.class)
        private final String STANDARDKITBILL;

        @JsonView(View.Summary.class)
        private final String ALIASITEMNO;

        @JsonView(View.Summary.class)
        private final String CUSTOMERACTION;

        @JsonView(View.Summary.class)
        private final String ITEMACTION;

        @JsonView(View.Summary.class)
        private final String WARRANTYCODE;

        @JsonView(View.Summary.class)
        private final Date EXPIRATIONDATE;

        @JsonView(View.Summary.class)
        private final String EXPIRATIONOVERRIDDEN;

        @JsonView(View.Summary.class)
        private final String COSTCODE;

        @JsonView(View.Summary.class)
        private final String COSTTYPE;

        @JsonView(View.Summary.class)
        private final String COMMENTTEXT;

        @JsonView(View.Summary.class)
        private final Date PROMISEDATE;

        @JsonView(View.Summary.class)
        private final Double QUANTITYSHIPPED;

        @JsonView(View.Summary.class)
        private final Double QUANTITYORDERED;

        @JsonView(View.Summary.class)
        private final Double QUANTITYBACKORDERED;

        @JsonView(View.Summary.class)
        private final Double UNITPRICE;

        @JsonView(View.Summary.class)
        private final Double UNITCOST;

        @JsonView(View.Summary.class)
        private final Double UNITOFMEASURECONVFACTOR;

        @JsonView(View.Summary.class)
        private final Double COMMISSIONAMT;

        @JsonView(View.Summary.class)
        private final Double LINEDISCOUNTPERCENT;

        @JsonView(View.Summary.class)
        private final Double QUANTITYPERBILL;

        @JsonView(View.Summary.class)
        private final Double EXTENSIONAMT;

        @JsonView(View.Summary.class)
        private final String ARMC_234_ENTRYCURRENCY;

        @JsonView(View.Summary.class)
        private final Double ARMC_234_ENTRYRATE;

        @JsonView(View.Summary.class)
        private final Double ARMC_234_BASEUNITCOST;

        @JsonView(View.Summary.class)
        private final Double ARMC_234_BASECOMMISSIONAMT;

        @JsonView(View.Summary.class)
        private final String APDIVISIONNO;

        @JsonView(View.Summary.class)
        private final String VENDORNO;

        @JsonView(View.Summary.class)
        private final String PURCHASEORDERNO;

        @JsonView(View.Summary.class)
        private final Date PURCHASEORDERREQUIREDDATE;

        @JsonView(View.Summary.class)
        private final String COMMODITYCODE;

        @JsonView(View.Summary.class)
        private final String ALTERNATETAXIDENTIFIER;

        @JsonView(View.Summary.class)
        private final String TAXTYPEAPPLIED;

        @JsonView(View.Summary.class)
        private final String NETGROSSINDICATOR;

        @JsonView(View.Summary.class)
        private final String DEBITCREDITINDICATOR;

        @JsonView(View.Summary.class)
        private final Double TAXAMT;

        @JsonView(View.Summary.class)
        private final Double TAXRATE;

        public Record(String INVOICENO, String HEADERSEQNO, String DETAILSEQNO, String ITEMCODE, String ITEMTYPE,
                String ITEMCODEDESC, String EXTENDEDDESCRIPTIONKEY, String SALESACCTKEY, String COSTOFGOODSSOLDACCTKEY,
                String INVENTORYACCTKEY, String UNITOFMEASURE, String SUBJECTTOEXEMPTION, String COMMISSIONABLE,
                String TAXCLASS, String DISCOUNT, String DROPSHIP, String WAREHOUSECODE, String PRICELEVEL,
                String PRODUCTLINE, String VALUATION, String PRICEOVERRIDDEN, String ORDERWAREHOUSE, String REVISION,
                String BILLOPTION1, String BILLOPTION2, String BILLOPTION3, String BILLOPTION4, String BILLOPTION5,
                String BILLOPTION6, String BILLOPTION7, String BILLOPTION8, String BILLOPTION9, String KITITEM,
                String EXPLODEDKITITEM, String SKIPPRINTCOMPLINE, String STANDARDKITBILL, String ALIASITEMNO,
                String CUSTOMERACTION, String ITEMACTION, String WARRANTYCODE, Date EXPIRATIONDATE,
                String EXPIRATIONOVERRIDDEN, String COSTCODE, String COSTTYPE, String COMMENTTEXT, Date PROMISEDATE,
                Double QUANTITYSHIPPED, Double QUANTITYORDERED, Double QUANTITYBACKORDERED, Double UNITPRICE,
                Double UNITCOST, Double UNITOFMEASURECONVFACTOR, Double COMMISSIONAMT, Double LINEDISCOUNTPERCENT,
                Double QUANTITYPERBILL, Double EXTENSIONAMT, String ARMC_234_ENTRYCURRENCY, Double ARMC_234_ENTRYRATE,
                Double ARMC_234_BASEUNITCOST, Double ARMC_234_BASECOMMISSIONAMT, String APDIVISIONNO, String VENDORNO,
                String PURCHASEORDERNO, Date PURCHASEORDERREQUIREDDATE, String COMMODITYCODE,
                String ALTERNATETAXIDENTIFIER, String TAXTYPEAPPLIED, String NETGROSSINDICATOR,
                String DEBITCREDITINDICATOR, Double TAXAMT, Double TAXRATE) {
            this.INVOICENO = INVOICENO;
            this.HEADERSEQNO = HEADERSEQNO;
            this.DETAILSEQNO = DETAILSEQNO;
            this.ITEMCODE = ITEMCODE;
            this.ITEMTYPE = ITEMTYPE;
            this.ITEMCODEDESC = ITEMCODEDESC;
            this.EXTENDEDDESCRIPTIONKEY = EXTENDEDDESCRIPTIONKEY;
            this.SALESACCTKEY = SALESACCTKEY;
            this.COSTOFGOODSSOLDACCTKEY = COSTOFGOODSSOLDACCTKEY;
            this.INVENTORYACCTKEY = INVENTORYACCTKEY;
            this.UNITOFMEASURE = UNITOFMEASURE;
            this.SUBJECTTOEXEMPTION = SUBJECTTOEXEMPTION;
            this.COMMISSIONABLE = COMMISSIONABLE;
            this.TAXCLASS = TAXCLASS;
            this.DISCOUNT = DISCOUNT;
            this.DROPSHIP = DROPSHIP;
            this.WAREHOUSECODE = WAREHOUSECODE;
            this.PRICELEVEL = PRICELEVEL;
            this.PRODUCTLINE = PRODUCTLINE;
            this.VALUATION = VALUATION;
            this.PRICEOVERRIDDEN = PRICEOVERRIDDEN;
            this.ORDERWAREHOUSE = ORDERWAREHOUSE;
            this.REVISION = REVISION;
            this.BILLOPTION1 = BILLOPTION1;
            this.BILLOPTION2 = BILLOPTION2;
            this.BILLOPTION3 = BILLOPTION3;
            this.BILLOPTION4 = BILLOPTION4;
            this.BILLOPTION5 = BILLOPTION5;
            this.BILLOPTION6 = BILLOPTION6;
            this.BILLOPTION7 = BILLOPTION7;
            this.BILLOPTION8 = BILLOPTION8;
            this.BILLOPTION9 = BILLOPTION9;
            this.KITITEM = KITITEM;
            this.EXPLODEDKITITEM = EXPLODEDKITITEM;
            this.SKIPPRINTCOMPLINE = SKIPPRINTCOMPLINE;
            this.STANDARDKITBILL = STANDARDKITBILL;
            this.ALIASITEMNO = ALIASITEMNO;
            this.CUSTOMERACTION = CUSTOMERACTION;
            this.ITEMACTION = ITEMACTION;
            this.WARRANTYCODE = WARRANTYCODE;
            this.EXPIRATIONDATE = EXPIRATIONDATE;
            this.EXPIRATIONOVERRIDDEN = EXPIRATIONOVERRIDDEN;
            this.COSTCODE = COSTCODE;
            this.COSTTYPE = COSTTYPE;
            this.COMMENTTEXT = COMMENTTEXT;
            this.PROMISEDATE = PROMISEDATE;
            this.QUANTITYSHIPPED = QUANTITYSHIPPED;
            this.QUANTITYORDERED = QUANTITYORDERED;
            this.QUANTITYBACKORDERED = QUANTITYBACKORDERED;
            this.UNITPRICE = UNITPRICE;
            this.UNITCOST = UNITCOST;
            this.UNITOFMEASURECONVFACTOR = UNITOFMEASURECONVFACTOR;
            this.COMMISSIONAMT = COMMISSIONAMT;
            this.LINEDISCOUNTPERCENT = LINEDISCOUNTPERCENT;
            this.QUANTITYPERBILL = QUANTITYPERBILL;
            this.EXTENSIONAMT = EXTENSIONAMT;
            this.ARMC_234_ENTRYCURRENCY = ARMC_234_ENTRYCURRENCY;
            this.ARMC_234_ENTRYRATE = ARMC_234_ENTRYRATE;
            this.ARMC_234_BASEUNITCOST = ARMC_234_BASEUNITCOST;
            this.ARMC_234_BASECOMMISSIONAMT = ARMC_234_BASECOMMISSIONAMT;
            this.APDIVISIONNO = APDIVISIONNO;
            this.VENDORNO = VENDORNO;
            this.PURCHASEORDERNO = PURCHASEORDERNO;
            this.PURCHASEORDERREQUIREDDATE = PURCHASEORDERREQUIREDDATE;
            this.COMMODITYCODE = COMMODITYCODE;
            this.ALTERNATETAXIDENTIFIER = ALTERNATETAXIDENTIFIER;
            this.TAXTYPEAPPLIED = TAXTYPEAPPLIED;
            this.NETGROSSINDICATOR = NETGROSSINDICATOR;
            this.DEBITCREDITINDICATOR = DEBITCREDITINDICATOR;
            this.TAXAMT = TAXAMT;
            this.TAXRATE = TAXRATE;
        }

        public String getINVOICENO() {
            return INVOICENO;
        }

        public String getHEADERSEQNO() {
            return HEADERSEQNO;
        }

        public String getDETAILSEQNO() {
            return DETAILSEQNO;
        }

        public String getITEMCODE() {
            return ITEMCODE;
        }

        public String getITEMTYPE() {
            return ITEMTYPE;
        }

        public String getITEMCODEDESC() {
            return ITEMCODEDESC;
        }

        public String getEXTENDEDDESCRIPTIONKEY() {
            return EXTENDEDDESCRIPTIONKEY;
        }

        public String getSALESACCTKEY() {
            return SALESACCTKEY;
        }

        public String getCOSTOFGOODSSOLDACCTKEY() {
            return COSTOFGOODSSOLDACCTKEY;
        }

        public String getINVENTORYACCTKEY() {
            return INVENTORYACCTKEY;
        }

        public String getUNITOFMEASURE() {
            return UNITOFMEASURE;
        }

        public String getSUBJECTTOEXEMPTION() {
            return SUBJECTTOEXEMPTION;
        }

        public String getCOMMISSIONABLE() {
            return COMMISSIONABLE;
        }

        public String getTAXCLASS() {
            return TAXCLASS;
        }

        public String getDISCOUNT() {
            return DISCOUNT;
        }

        public String getDROPSHIP() {
            return DROPSHIP;
        }

        public String getWAREHOUSECODE() {
            return WAREHOUSECODE;
        }

        public String getPRICELEVEL() {
            return PRICELEVEL;
        }

        public String getPRODUCTLINE() {
            return PRODUCTLINE;
        }

        public String getVALUATION() {
            return VALUATION;
        }

        public String getPRICEOVERRIDDEN() {
            return PRICEOVERRIDDEN;
        }

        public String getORDERWAREHOUSE() {
            return ORDERWAREHOUSE;
        }

        public String getREVISION() {
            return REVISION;
        }

        public String getBILLOPTION1() {
            return BILLOPTION1;
        }

        public String getBILLOPTION2() {
            return BILLOPTION2;
        }

        public String getBILLOPTION3() {
            return BILLOPTION3;
        }

        public String getBILLOPTION4() {
            return BILLOPTION4;
        }

        public String getBILLOPTION5() {
            return BILLOPTION5;
        }

        public String getBILLOPTION6() {
            return BILLOPTION6;
        }

        public String getBILLOPTION7() {
            return BILLOPTION7;
        }

        public String getBILLOPTION8() {
            return BILLOPTION8;
        }

        public String getBILLOPTION9() {
            return BILLOPTION9;
        }

        public String getKITITEM() {
            return KITITEM;
        }

        public String getEXPLODEDKITITEM() {
            return EXPLODEDKITITEM;
        }

        public String getSKIPPRINTCOMPLINE() {
            return SKIPPRINTCOMPLINE;
        }

        public String getSTANDARDKITBILL() {
            return STANDARDKITBILL;
        }

        public String getALIASITEMNO() {
            return ALIASITEMNO;
        }

        public String getCUSTOMERACTION() {
            return CUSTOMERACTION;
        }

        public String getITEMACTION() {
            return ITEMACTION;
        }

        public String getWARRANTYCODE() {
            return WARRANTYCODE;
        }

        public Date getEXPIRATIONDATE() {
            return EXPIRATIONDATE;
        }

        public String getEXPIRATIONOVERRIDDEN() {
            return EXPIRATIONOVERRIDDEN;
        }

        public String getCOSTCODE() {
            return COSTCODE;
        }

        public String getCOSTTYPE() {
            return COSTTYPE;
        }

        public String getCOMMENTTEXT() {
            return COMMENTTEXT;
        }

        public Date getPROMISEDATE() {
            return PROMISEDATE;
        }

        public Double getQUANTITYSHIPPED() {
            return QUANTITYSHIPPED;
        }

        public Double getQUANTITYORDERED() {
            return QUANTITYORDERED;
        }

        public Double getQUANTITYBACKORDERED() {
            return QUANTITYBACKORDERED;
        }

        public Double getUNITPRICE() {
            return UNITPRICE;
        }

        public Double getUNITCOST() {
            return UNITCOST;
        }

        public Double getUNITOFMEASURECONVFACTOR() {
            return UNITOFMEASURECONVFACTOR;
        }

        public Double getCOMMISSIONAMT() {
            return COMMISSIONAMT;
        }

        public Double getLINEDISCOUNTPERCENT() {
            return LINEDISCOUNTPERCENT;
        }

        public Double getQUANTITYPERBILL() {
            return QUANTITYPERBILL;
        }

        public Double getEXTENSIONAMT() {
            return EXTENSIONAMT;
        }

        public String getARMC_234_ENTRYCURRENCY() {
            return ARMC_234_ENTRYCURRENCY;
        }

        public Double getARMC_234_ENTRYRATE() {
            return ARMC_234_ENTRYRATE;
        }

        public Double getARMC_234_BASEUNITCOST() {
            return ARMC_234_BASEUNITCOST;
        }

        public Double getARMC_234_BASECOMMISSIONAMT() {
            return ARMC_234_BASECOMMISSIONAMT;
        }

        public String getAPDIVISIONNO() {
            return APDIVISIONNO;
        }

        public String getVENDORNO() {
            return VENDORNO;
        }

        public String getPURCHASEORDERNO() {
            return PURCHASEORDERNO;
        }

        public Date getPURCHASEORDERREQUIREDDATE() {
            return PURCHASEORDERREQUIREDDATE;
        }

        public String getCOMMODITYCODE() {
            return COMMODITYCODE;
        }

        public String getALTERNATETAXIDENTIFIER() {
            return ALTERNATETAXIDENTIFIER;
        }

        public String getTAXTYPEAPPLIED() {
            return TAXTYPEAPPLIED;
        }

        public String getNETGROSSINDICATOR() {
            return NETGROSSINDICATOR;
        }

        public String getDEBITCREDITINDICATOR() {
            return DEBITCREDITINDICATOR;
        }

        public Double getTAXAMT() {
            return TAXAMT;
        }

        public Double getTAXRATE() {
            return TAXRATE;
        }
    }

    @JsonView(View.Summary.class)
    private final Key key;

    @JsonView(View.Summary.class)
    private final List<Record> records;

    public ArInvoiceHistoryDetailDto(Key key, List<Record> records) {
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
