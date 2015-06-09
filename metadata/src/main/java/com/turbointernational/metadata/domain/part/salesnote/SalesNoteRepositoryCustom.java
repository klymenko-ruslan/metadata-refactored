package com.turbointernational.metadata.domain.part.salesnote;

import org.springframework.data.domain.Page;

/**
 *
 * @author jrodriguez
 */
public interface SalesNoteRepositoryCustom {
    Page<SalesNote> search(SalesNoteSearchRequest request);
}
