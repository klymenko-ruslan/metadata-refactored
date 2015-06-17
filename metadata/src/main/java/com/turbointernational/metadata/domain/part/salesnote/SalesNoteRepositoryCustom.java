package com.turbointernational.metadata.domain.part.salesnote;

import com.turbointernational.metadata.domain.part.salesnote.dao.SalesNoteSearchRequest;
import org.springframework.data.domain.Page;

/**
 *
 * @author jrodriguez
 */
public interface SalesNoteRepositoryCustom {
    Page<SalesNote> search(SalesNoteSearchRequest request);
}
