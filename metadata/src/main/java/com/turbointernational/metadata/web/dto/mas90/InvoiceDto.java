package com.turbointernational.metadata.web.dto.mas90;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author dmitry.trunikov@zorallabs.com
 */
@JsonInclude(NON_NULL)
public class InvoiceDto {

    @JsonInclude(NON_NULL)
    static class DetailsDto {

        private long partId;

        private String partNumber;

        private long[] interchanges;

        private String descriptionMas90;

        private String descriptionMetadata;

        public DetailsDto() {
        }

        public DetailsDto(long partId, String partNumber, long[] interchanges, String descriptionMas90, String descriptionMetadata) {
            this.partId = partId;
            this.interchanges = interchanges;
            this.descriptionMas90 = descriptionMas90;
            this.descriptionMetadata = descriptionMetadata;
        }

        public long getPartId() {
            return partId;
        }

        public void setPartId(long partId) {
            this.partId = partId;
        }

        public String getPartNumber() {
            return partNumber;
        }

        public void setPartNumber(String partNumber) {
            this.partNumber = partNumber;
        }

        public long[] getInterchanges() {
            return interchanges;
        }

        public void setInterchanges(long[] interchanges) {
            this.interchanges = interchanges;
        }

        public String getDescriptionMas90() {
            return descriptionMas90;
        }

        public void setDescriptionMas90(String descriptionMas90) {
            this.descriptionMas90 = descriptionMas90;
        }

        public String getDescriptionMetadata() {
            return descriptionMetadata;
        }

        public void setDescriptionMetadata(String descriptionMetadata) {
            this.descriptionMetadata = descriptionMetadata;
        }

    }

    private String no;

    private long date;

    private String customerNo;

    private List<DetailsDto> details;

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
