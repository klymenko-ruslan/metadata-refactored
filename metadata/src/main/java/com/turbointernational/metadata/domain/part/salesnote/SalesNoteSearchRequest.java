package com.turbointernational.metadata.domain.part.salesnote;

import com.google.common.collect.Sets;
import java.util.Set;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author jrodriguez
 */
public class SalesNoteSearchRequest {
    private static final int DEFAULT_PAGE_SIZE = 20;
    
    private PageRequest page = new PageRequest(0, DEFAULT_PAGE_SIZE);
    
    private Long primaryPartId;
    private String query;
    
    private boolean includePrimary = true;
    private boolean includeRelated = false;
    
    private Set<SalesNoteState> states = Sets.newHashSet();
}
