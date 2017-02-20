package com.turbointernational.metadata.service;

import com.turbointernational.metadata.dao.ServiceDao;
import com.turbointernational.metadata.entity.Changelog.ServiceEnum;
import com.turbointernational.metadata.web.dto.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-15.
 */
@Service
public class ServiceService {

    @Autowired
    private ServiceDao serviceDao;

    @Secured("ROLE_READ")
    public List<com.turbointernational.metadata.entity.Service> getAll() {
        return serviceDao.findAll();
    }

    @Secured("ROLE_READ")
    public boolean isChangelogSourceRequired(ServiceEnum service) {
        long serviceId = service.ordinal();
        return serviceDao.isChangelogSourceRequired(serviceId);
    }

    @Secured("ROLE_READ")
    public Page<com.turbointernational.metadata.entity.Service> filter(String sortProperty, String sortOrder,
                                                                       Integer offset, Integer limit) {
        return serviceDao.filter(sortProperty, sortOrder, offset, limit);
    }

    @Secured("ROLE_SERVICE")
    public void setChangelogSourceRequired(Long serviceId, Boolean required) {
        serviceDao.setChangelogSourceRequired(serviceId, required);
    }

}
