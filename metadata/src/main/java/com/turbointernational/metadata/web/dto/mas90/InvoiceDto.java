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
        private List<Long> interchanges;

        @JsonView(View.Summary.class)
        private String description;

        public DetailsDto() {
        }

        public DetailsDto(Long partId, String partNumber, List<Long> interchanges, String description) {
            this.partId = partId;
            this.interchanges = interchanges;
            this.description = description;
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

    }

    @JsonView(View.Summary.class)
    private String no;

    @JsonView(View.Summary.class)
    private long date;

    @JsonView(View.Summary.class)
    private String customerNo;

    @JsonView(View.Summary.class)
    private List<DetailsDto> details;

    public InvoiceDto() {
    }

    public InvoiceDto(String no, long date, String customerNo, List<DetailsDto> details) {
        this.no = no;
        this.date = date;
        this.customerNo = customerNo;
        this.details = details;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
