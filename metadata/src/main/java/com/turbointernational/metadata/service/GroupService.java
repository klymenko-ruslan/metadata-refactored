package com.turbointernational.metadata.service;

import static java.util.Optional.ofNullable;

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

    public Page<Group> filter(String fltrName, String fltrRole, Boolean fltrIsMemeber, String sortProperty,
            String sortOrder, Integer offset, Integer limit) {
        return groupDao.filter(ofNullable(fltrName), ofNullable(fltrRole), ofNullable(fltrIsMemeber),
                ofNullable(sortProperty), ofNullable(sortOrder), ofNullable(offset), ofNullable(limit));
    }

}
