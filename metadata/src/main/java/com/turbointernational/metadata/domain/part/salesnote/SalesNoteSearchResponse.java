package com.turbointernational.metadata.domain.part.salesnote;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import java.util.List;

/**
 *
 * @author jrodriguez
 */
public class SalesNoteSearchResponse {
    @JsonView(View.Summary.class)
    private final long total;
    
    @JsonView(View.Summary.class)
    private final long pageSize;
    
    @JsonView(View.Summary.class)
    private final long page;
    
    @JsonView(View.Summary.class)
    private final List<SalesNote> content;

    public SalesNoteSearchResponse(long total, long pageSize, long page, List<SalesNote> salesNotes) {
        this.total = total;
        this.pageSize = pageSize;
        this.page = page;
        this.content = salesNotes;
    }

    public long getPage() {
        return page;
    }

    public long getPageSize() {
        return pageSize;
    }

    public List<SalesNote> getContent() {
        return content;
    }

    public long getTotal() {
        return total;
    }
    
}
