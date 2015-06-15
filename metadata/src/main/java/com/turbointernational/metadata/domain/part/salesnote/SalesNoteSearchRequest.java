package com.turbointernational.metadata.domain.part.salesnote;

import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author jrodriguez
 */

/*
SELECT sn
FROM SalesNote sn
JOIN sn.salesNoteParts snp
WHERE
  sn.state IN :states
  AND (:primaryPartId IS NULL
       OR snp)
  AND (snp.id = :partId OR snp.id = :query)
  AND sn.

*/
public class SalesNoteSearchRequest implements Serializable {
    private static final int DEFAULT_PAGE_SIZE = 20;
    
    private PageRequest page = new PageRequest(0, DEFAULT_PAGE_SIZE);
    
    private Long primaryPartId;
    private String query;
    
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
    
    public PageRequest getPage() {
        return page;
    }

    public void setPage(PageRequest page) {
        this.page = page;
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
