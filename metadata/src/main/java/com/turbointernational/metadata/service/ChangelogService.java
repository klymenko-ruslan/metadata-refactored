package com.turbointernational.metadata.service;

import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.dao.ChangelogDao;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.web.dto.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 09.08.16.
 */
@Service
public class ChangelogService {

    @Autowired
    private ChangelogDao changelogDao;

    public Changelog log(String description) {
        User user = User.getCurrentUser();
        return log(user, description, "");
    }

    public Changelog log(User user, String description) {
        return log(user, description, "");
    }

    public Changelog log(String description, String data) {
        User user = User.getCurrentUser();
        return log(user, description, data);
    }

    public Changelog log(User user, String description, String data) {
        return changelogDao.log(user, description, data);
    }

    public Changelog log(String description, Serializable data) {
        User user = User.getCurrentUser();
        return log(user, description, data);
    }

    public Changelog log(User user, String description, Serializable data) {
        return changelogDao.log(user, description, data);
    }

    public Page<Changelog> filter(Long userId, Calendar startDate, Calendar finishDate,
                                  String description, String data,
                                  String sortProperty, String sortOrder,
                                  Integer offset, Integer limit) {
        // Normalizaton of the time range.
        if (startDate != null && finishDate != null && startDate.compareTo(finishDate) > 0) {
            Calendar swap = finishDate;
            finishDate = startDate;
            startDate = swap;
        }
        Date d0 = null;
        Date d1 = null;
        // Set start of a day for the startDate and enf of a day for endDate.
        if (startDate != null) {
            startDate.clear(HOUR_OF_DAY);
            startDate.clear(MINUTE);
            startDate.clear(SECOND);
            startDate.clear(MILLISECOND);
            d0 = startDate.getTime();
        }
        if (finishDate != null) {
            finishDate.set(HOUR_OF_DAY, 23);
            finishDate.set(MINUTE, 59);
            finishDate.set(SECOND, 59);
            finishDate.set(MILLISECOND, 999);
            d1 = finishDate.getTime();
        }
        return changelogDao.filter(userId, d0, d1, description, data, sortProperty, sortOrder, offset, limit);
    }

}