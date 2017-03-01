package com.turbointernational.metadata.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 *
 * @author jrodriguez
 */
public class CreateSalesNoteRequest implements Serializable {

    private static final long serialVersionUID = -4604795204191095582L;

    @NotNull
    @JsonView(View.Summary.class)
    private Long primaryPartId;

    @NotNull
    @JsonView(View.Summary.class)
    private String comment;

    /**
     * Changelog source IDs which should be linked to the changelog. See ticket
     * #891 for details.
     */
    @JsonView(View.Summary.class)
    private Long[] sourcesIds;

    @JsonView(View.Summary.class)
    private Integer[] chlogSrcRatings;

    @JsonView(View.Summary.class)
    private String chlogSrcLnkDescription;

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

    public Long[] getSourcesIds() {
        return sourcesIds;
    }

    public void setSourcesIds(Long[] sourcesIds) {
        this.sourcesIds = sourcesIds;
    }

    public Integer[] getChlogSrcRatings() {
        return chlogSrcRatings;
    }

    public void setChlogSrcRatings(Integer[] chlogSrcRatings) {
        this.chlogSrcRatings = chlogSrcRatings;
    }

    public String getChlogSrcLnkDescription() {
        return chlogSrcLnkDescription;
    }

    public void setChlogSrcLnkDescription(String chlogSrcLnkDescription) {
        this.chlogSrcLnkDescription = chlogSrcLnkDescription;
    }

}
