package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.changelog.Changelog;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dmytro.trunykov@zorallabs.com on 09.08.16.
 */
@Service
public class ChangelogService {

    @Autowired
    private ChangelogDao changelogDao;

    public Page<Changelog> filter(Long userId, Date startDate, Date finishDate, String description,
                                  String sortProperty, String sortOrder,
                                  Integer offset, Integer limit) {
        // Normalizaton of the time range.
        if (startDate != null && finishDate != null && startDate.compareTo(finishDate) > 0) {
            Date swap = finishDate;
            finishDate = startDate;
            startDate = swap;
        }
        return changelogDao.filter(userId, startDate, finishDate, description, sortProperty, sortOrder,
                offset, limit);
    }

}
