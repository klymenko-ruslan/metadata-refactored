package com.turbointernational.metadata.web.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jrodriguez
 */
public class UpdateSalesNoteRequest implements Serializable {
    
    @NotNull
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
}
