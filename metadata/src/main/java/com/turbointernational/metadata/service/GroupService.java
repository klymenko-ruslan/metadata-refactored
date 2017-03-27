package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.dao.GroupDao.ALIAS_GROUP;
import static com.turbointernational.metadata.dao.GroupDao.ALIAS_MEMBER;
import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.GroupDao;
import com.turbointernational.metadata.entity.Group;
import com.turbointernational.metadata.web.dto.Page;
import com.turbointernational.metadata.web.dto.GroupMemberDto;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class GroupService {

    @Autowired
    private GroupDao groupDao;

    public Page<GroupMemberDto> filter(Long userId, String fltrName, String fltrRole, Boolean fltrIsMemeber,
            String sortProperty, String sortOrder, Integer offset, Integer limit) {
        Page<Tuple> page = groupDao.filter(userId, ofNullable(fltrName), ofNullable(fltrRole),
                ofNullable(fltrIsMemeber), ofNullable(sortProperty), ofNullable(sortOrder), ofNullable(offset),
                ofNullable(limit));
        List<GroupMemberDto> dtos = page.getRecs().stream().map(t -> {
            Group group = t.get(ALIAS_GROUP, Group.class);
            List<String> roles = group.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
            Boolean member = t.get(ALIAS_MEMBER, Boolean.class);
            return new GroupMemberDto(group.getName(), roles, member);
        }).collect(Collectors.toList());
        return new Page<GroupMemberDto>(page.getTotal(), dtos);
    }

}
