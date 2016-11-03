package com.turbointernational.metadata.entity;

import com.turbointernational.metadata.web.dto.SalesNoteSearchRequest;

/**
 *
 * @author jrodriguez
 */
public interface SalesNoteRepositoryCustom {
    SalesNoteSearchResponse search(SalesNoteSearchRequest request);
}
