package com.turbointernational.mas90.entity;

import java.io.Serializable;

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
    private Double quantityshipped;

    @Column(name = "EXTENSIONAMT", insertable = false, updatable = false, precision = 12, scale = 2)
    private Double extensionamt;

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

    public Double getQuantityshipped() {
        return quantityshipped;
    }

    public void setQuantityshipped(Double quantityshipped) {
        this.quantityshipped = quantityshipped;
    }

    public Double getExtensionamt() {
        return extensionamt;
    }

    public void setExtensionamt(Double extensionamt) {
        this.extensionamt = extensionamt;
    }

}
