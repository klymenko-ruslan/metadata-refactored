package com.turbointernational.metadata.web.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;
import com.turbointernational.metadata.entity.SalesNoteState;

/**
 *
 * @author jrodriguez
 */

public class SalesNoteSearchRequest implements Serializable {

    private static final long serialVersionUID = -3668345033591679100L;

    private Long primaryPartId;
    private String query;
    private int page = 0;
    private int pageSize = 20;

    private boolean includePrimary = true;
    private boolean includeRelated = true;

    private Set<SalesNoteState> states = Sets.newHashSet();

    public SalesNoteSearchRequest() {
    }

    public SalesNoteSearchRequest(Long primaryPartId, String query, SalesNoteState... states) {
        this.primaryPartId = primaryPartId;
        this.query = query;
        this.states.addAll(Arrays.asList(states));
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPrimaryPartId() {
        return primaryPartId;
    }

    public void setPrimaryPartId(Long primaryPartId) {
        this.primaryPartId = primaryPartId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isIncludePrimary() {
        return includePrimary;
    }

    public void setIncludePrimary(boolean includePrimary) {
        this.includePrimary = includePrimary;
    }

    public boolean isIncludeRelated() {
        return includeRelated;
    }

    public void setIncludeRelated(boolean includeRelated) {
        this.includeRelated = includeRelated;
    }

    public Set<SalesNoteState> getStates() {
        return states;
    }

    public void setStates(Set<SalesNoteState> states) {
        this.states = states;
    }

    public String getFormattedQuery() {
        if (StringUtils.isBlank(query)) {
            return null;
        }

        return "%" + query.toLowerCase() + "%";
    }
}
