package com.turbointernational.metadata.web.dto.mas90;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmitry.trunikov@zorallabs.com
 */
@JsonInclude(ALWAYS)
public class InvoiceDto {

    @JsonInclude(NON_NULL)
    public static class DetailsDto {

        @JsonView(View.Summary.class)
        private Long partId;

        @JsonView(View.Summary.class)
        private String partNumber;

        @JsonView(View.Summary.class)
        private String partTypeName;

        @JsonView(View.Summary.class)
        private List<Long> interchanges;

        @JsonView(View.Summary.class)
        private String description;

        @JsonView(View.Summary.class)
        private String detailSeqNo;

        @JsonView(View.Summary.class)
        private Integer quantityShipped;

        @JsonView(View.Summary.class)
        private Double unitPrice;

        @JsonView(View.Summary.class)
        private Double unitCost;

        @JsonView(View.Summary.class)
        private Double extensionAmt;

        @JsonView(View.Summary.class)
        private String armc234EntryCurrency;

        @JsonView(View.Summary.class)
        private Double armc234EntryRate;

        public DetailsDto() {
        }

        public DetailsDto(Long partId, String partNumber, String partTypeName, List<Long> interchanges,
                String description, String detailSeqNo, Integer quantityShipped, Double unitPrice,
                Double unitCost, Double extensionAmt, String armc234EntryCurrency, Double armc234EntryRate) {
            this.partId = partId;
            this.partNumber = partNumber;
            this.partTypeName = partTypeName;
            this.interchanges = interchanges;
            this.description = description;
            this.detailSeqNo = detailSeqNo;
            this.quantityShipped = quantityShipped;
            this.unitPrice = unitPrice;
            this.unitCost = unitCost;
            this.extensionAmt = extensionAmt;
            this.armc234EntryCurrency = armc234EntryCurrency;
            this.armc234EntryRate = armc234EntryRate;
        }

        public Long getPartId() {
            return partId;
        }

        public void setPartId(Long partId) {
            this.partId = partId;
        }

        public String getPartNumber() {
            return partNumber;
        }

        public void setPartNumber(String partNumber) {
            this.partNumber = partNumber;
        }

        public String getPartTypeName() {
            return partTypeName;
        }

        public void setPartTypeName(String partTypeName) {
            this.partTypeName = partTypeName;
        }

        public List<Long> getInterchanges() {
            return interchanges;
        }

        public void setInterchanges(List<Long> interchanges) {
            this.interchanges = interchanges;
        }

        public String getDescriptionMas90() {
            return description;
        }

        public void setDescriptionMas90(String descriptionMas90) {
            this.description = descriptionMas90;
        }

        public String getDetailSeqNo() {
            return detailSeqNo;
        }

        public void setDetailSeqNo(String detailSeqNo) {
            this.detailSeqNo = detailSeqNo;
        }

        public Integer getQuantityShipped() {
            return quantityShipped;
        }

        public void setQuantityShipped(Integer quantityShipped) {
            this.quantityShipped = quantityShipped;
        }

        public Double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(Double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public Double getUnitCost() {
            return unitCost;
        }

        public void setUnitCost(Double unitCost) {
            this.unitCost = unitCost;
        }

        public Double getExtensionAmt() {
            return extensionAmt;
        }

        public void setExtensionAmt(Double extensionAmt) {
            this.extensionAmt = extensionAmt;
        }

        public String getArmc234EntryCurrency() {
            return armc234EntryCurrency;
        }

        public void setArmc234EntryCurrency(String armc234EntryCurrency) {
            this.armc234EntryCurrency = armc234EntryCurrency;
        }

        public Double getArmc234EntryRate() {
            return armc234EntryRate;
        }

        public void setArmc234EntryRate(Double armc234EntryRate) {
            this.armc234EntryRate = armc234EntryRate;
        }

    }

    @JsonView(View.Summary.class)
    private String no;

    @JsonView(View.Summary.class)
    private String headerSeqNo;

    @JsonView(View.Summary.class)
    private Long date;

    @JsonView(View.Summary.class)
    private Long updated;

    @JsonView(View.Summary.class)
    private String customerNo;

    @JsonView(View.Summary.class)
    private List<DetailsDto> details;

    public InvoiceDto() {
    }

    public InvoiceDto(String no, String headerSeqNo, Long date, Long updated, String customerNo,
            List<DetailsDto> details) {
        this.no = no;
        this.headerSeqNo = headerSeqNo;
        this.date = date;
        this.updated = updated;
        this.customerNo = customerNo;
        this.details = details;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getHeaderSeqNo() {
        return headerSeqNo;
    }

    public void setHeaderSeqNo(String headerSeqNo) {
        this.headerSeqNo = headerSeqNo;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public List<DetailsDto> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsDto> details) {
        this.details = details;
    }

}
