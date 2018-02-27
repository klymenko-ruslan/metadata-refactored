package com.turbointernational.metadata.service;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.dao.ChangelogDao;
import com.turbointernational.metadata.dao.ChangelogPartDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.Changelog.ServiceEnum;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.web.dto.ChangelogAggregation;
import com.turbointernational.metadata.web.dto.Page;

/**
 * Created by dmytro.trunykov@zorallabs.com on 09.08.16.
 */
@Service
public class ChangelogService {

    private final static Logger logger = LoggerFactory.getLogger(ChangelogService.class);

    /**
     * A part that participates in an operation that is being registered as a record in the changelog.
     *
     * On the Web UI you can see those records on the 'Part Details' view in the tab 'Audit log'.
     */
    public static class RelatedPart {

        private Long partId;

        private ChangelogPart.Role role;

        public RelatedPart(Long partId, ChangelogPart.Role role) {
            this.partId = partId;
            this.role = role;
        }

        public Long getPartId() {
            return partId;
        }

        public void setPartId(Long partId) {
            this.partId = partId;
        }

        public ChangelogPart.Role getRole() {
            return role;
        }

        public void setRole(ChangelogPart.Role role) {
            this.role = role;
        }

    }

    @Autowired
    private ChangelogDao changelogDao;

    @Autowired
    private ChangelogPartDao changelogPartDao;

    @Autowired
    private PartDao partDao;

    @Autowired
    private ObjectMapper json;

    public Changelog log(ServiceEnum service, String description, Collection<RelatedPart> relatedParts) {
        User user = User.getCurrentUser();
        return log(service, user, description, "", relatedParts);
    }

    public Changelog log(ServiceEnum service, User user, String description, Collection<RelatedPart> relatedParts) {
        return log(service, user, description, "", relatedParts);
    }

    public Changelog log(ServiceEnum service, String description, Serializable data,
            Collection<RelatedPart> relatedParts) {
        User user = User.getCurrentUser();
        return log(service, user, description, data, relatedParts);
    }

    public Changelog log(ServiceEnum service, User user, String description, Serializable data,
            Collection<RelatedPart> relatedParts) {
        String dataNormalized;
        if (data == null) {
            dataNormalized = null;
        } else if (data instanceof String) {
            dataNormalized = (String) data;
        } else {
            try {
                dataNormalized = json.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                dataNormalized = "Could not serialize data: " + e.getMessage();
            }
        }
        String normDescription = description;
        boolean descrHasBeenNormalized = false;
        if (description != null && description.length() > 255) {
            normDescription = StringUtils.abbreviate(description, 255);
            descrHasBeenNormalized = true;
        }
        Changelog log = changelogDao.log(service, user, normDescription, dataNormalized);
        if (descrHasBeenNormalized) {
            logger.warn("Message to the audit log [{}] is too long and was cut. Original message: {}", log.getId(),
                    description);
        }
        if (relatedParts != null && !relatedParts.isEmpty()) {
            for (RelatedPart rp : relatedParts) {
                Part part = partDao.getReference(rp.getPartId());
                ChangelogPart chlgprt = new ChangelogPart(log, part, rp.getRole());
                changelogPartDao.persist(chlgprt);
            }
        }
        return log;
    }

    public Page<Changelog> filter(List<ServiceEnum> services, List<Long> userIds, Calendar startDate, Calendar endDate,
            String description, String data, Long partId, String sortProperty, String sortOrder, Integer offset,
            Integer limit) {
        // Normalizaton of the time range.
        if (startDate != null && endDate != null && startDate.compareTo(endDate) > 0) {
            Calendar swap = endDate;
            endDate = startDate;
            startDate = swap;
        }
        Date d0 = null;
        Date d1 = null;
        // Set start of a day for the startDate and end of a day for endDate.
        if (startDate != null) {
            startDate.set(HOUR_OF_DAY, 0);
            startDate.set(MINUTE, 0);
            startDate.set(SECOND, 0);
            startDate.set(MILLISECOND, 0);
            d0 = startDate.getTime();
        }
        if (endDate != null) {
            endDate.set(HOUR_OF_DAY, 23);
            endDate.set(MINUTE, 59);
            endDate.set(SECOND, 59);
            endDate.set(MILLISECOND, 999);
            d1 = endDate.getTime();
        }
        return changelogDao.filter(services, userIds, d0, d1, description, data, partId, sortProperty, sortOrder,
                offset, limit);
    }

    public List<ChangelogAggregation> filterAggragation(Set<ServiceEnum> services, Set<Long> userIds, Calendar startDate, Calendar endDate,
            String description, String data) {
        Date d0 = null;
        Date d1 = null;
        // Set start of a day for the startDate and end of a day for endDate.
        if (startDate != null) {
            startDate.set(HOUR_OF_DAY, 0);
            startDate.set(MINUTE, 0);
            startDate.set(SECOND, 0);
            startDate.set(MILLISECOND, 0);
            d0 = startDate.getTime();
        }
        if (endDate != null) {
            endDate.set(HOUR_OF_DAY, 23);
            endDate.set(MINUTE, 59);
            endDate.set(SECOND, 59);
            endDate.set(MILLISECOND, 999);
            d1 = endDate.getTime();
        }
        return changelogDao.filterAggragation(services, userIds, d0, d1, description, data);
    }
}
