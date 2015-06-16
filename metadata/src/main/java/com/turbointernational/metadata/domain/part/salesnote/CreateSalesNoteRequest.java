package com.turbointernational.metadata.domain.part.salesnote;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jrodriguez
 */
public class CreateSalesNoteRequest implements Serializable {
    
    @NotNull
    private Long primaryPartId;
    
    @NotNull
    private String comment;

    public Long getPrimaryPartId() {
        return primaryPartId;
    }

    public void setPrimaryPartId(Long primaryPartId) {
        this.primaryPartId = primaryPartId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
}
