package com.turbointernational.metadata.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * @author jrodriguez
 */
public class UpdateSalesNoteRequest implements Serializable {

    private static final long serialVersionUID = -7660933380532503595L;

    @NotNull
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
