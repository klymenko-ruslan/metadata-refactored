package com.turbointernational.metadata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.GroupDao;
import com.turbointernational.metadata.entity.Group;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class GroupService {

    @Autowired
    private GroupDao groupDao;

    public Page<Group> filter(String fltrName, String fltrRole, String sortProperty, String sortOrder, Integer offset,
            Integer limit) {
        return groupDao.filter(fltrName, fltrRole, sortProperty, sortOrder, offset, limit);
    }

}
