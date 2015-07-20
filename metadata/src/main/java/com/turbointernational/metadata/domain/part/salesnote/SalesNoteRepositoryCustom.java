package com.turbointernational.metadata.domain.part.salesnote;

import com.turbointernational.metadata.domain.part.salesnote.dto.SalesNoteSearchRequest;

/**
 *
 * @author jrodriguez
 */
public interface SalesNoteRepositoryCustom {
    SalesNoteSearchResponse search(SalesNoteSearchRequest request);
}
