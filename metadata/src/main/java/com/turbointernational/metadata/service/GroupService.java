package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.dao.GroupDao.ALIAS_GROUP_ID;
import static com.turbointernational.metadata.dao.GroupDao.ALIAS_MEMBER;
import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.metadata.dao.GroupDao;
import com.turbointernational.metadata.dao.UserGroupDao;
import com.turbointernational.metadata.entity.Group;
import com.turbointernational.metadata.web.dto.GroupMember;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class GroupService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserGroupDao userGroupDao;

    public Page<GroupMember> filter(Long userId, String fltrName, String fltrRole, Boolean fltrIsMemeber,
            String sortProperty, String sortOrder, Integer offset, Integer limit) {
        Page<Tuple> page = groupDao.filter(userId, ofNullable(fltrName), ofNullable(fltrRole),
                ofNullable(fltrIsMemeber), ofNullable(sortProperty), ofNullable(sortOrder), ofNullable(offset),
                ofNullable(limit));
        List<GroupMember> dtos = page.getRecs().stream().map(t -> {
            Long groupId = t.get(ALIAS_GROUP_ID, Long.class);
            Group group = groupDao.findOne(groupId);
            List<String> roles = group.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
            roles.sort((s1, s2) -> s1.compareTo(s2));
            Boolean member = t.get(ALIAS_MEMBER, Boolean.class);
            return new GroupMember(group.getId(), group.getName(), roles, member);
        }).collect(Collectors.toList());
        return new Page<GroupMember>(page.getTotal(), dtos);
    }

    @Transactional
    public void updateMembership(Long userId, Long groupId, Boolean isMember) {
        if (isMember) {
            userGroupDao.addUserToGroup(userId, groupId);
        } else {
            userGroupDao.removeUserFromGroup(userId, groupId);
        }
    }

}
