package com.turbointernational.mas90.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Entity
@Table(name = "AR_INVOICEHISTORYDETAIL")
public class ArInvoiceHistoryDetail implements Serializable {

    private static final long serialVersionUID = 5410167400372464874L;

    @Id
    @Column(name = "INVOICENO", length = 7, nullable = false, insertable = false, updatable = false)
    private String invoiceno;

    @Id
    @Column(name = "HEADERSEQNO", length = 6, nullable = false, insertable = false, updatable = false)
    private String headerseqno;

    @Id
    @Column(name = "DETAILSEQNO", length = 6, nullable = false, insertable = false, updatable = false)
    private String detailseqno;

    @Column(name = "ITEMCODE", length = 30, insertable = false, updatable = false)
    private String itemcode;

    @Column(name = "QUANTITYSHIPPED", insertable = false, updatable = false, precision = 16, scale = 6)
    private BigDecimal quantityshipped;

    @Column(name = "EXTENSIONAMT", insertable = false, updatable = false, precision = 12, scale = 2)
    private BigDecimal extensionamt;

    @Column(name = "ARMC_234_ENTRYRATE", insertable = false, updatable = false, precision = 17, scale = 8)
    private BigDecimal armc234Entryrate;

    @Column(name = "PRODUCTLINE", insertable = false, updatable = false, length = 4)
    private String productLine;

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }

    public String getHeaderseqno() {
        return headerseqno;
    }

    public void setHeaderseqno(String headerseqno) {
        this.headerseqno = headerseqno;
    }

    public String getDetailseqno() {
        return detailseqno;
    }

    public void setDetailseqno(String detailseqno) {
        this.detailseqno = detailseqno;
    }

    public BigDecimal getQuantityshipped() {
        return quantityshipped;
    }

    public void setQuantityshipped(BigDecimal quantityshipped) {
        this.quantityshipped = quantityshipped;
    }

    public BigDecimal getExtensionamt() {
        return extensionamt;
    }

    public void setExtensionamt(BigDecimal extensionamt) {
        this.extensionamt = extensionamt;
    }

    public BigDecimal getArmc234Entryrate() {
        return armc234Entryrate;
    }

    public void setArmc234Entryrate(BigDecimal armc234Entryrate) {
        this.armc234Entryrate = armc234Entryrate;
    }

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

}
