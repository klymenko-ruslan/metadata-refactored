package com.turbointernational.metadata.web.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.GroupDao;
import com.turbointernational.metadata.dao.RoleDao;
import com.turbointernational.metadata.dao.UserDao;
import com.turbointernational.metadata.entity.Group;
import com.turbointernational.metadata.entity.Role;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.service.GroupService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.GroupMemberDto;
import com.turbointernational.metadata.web.dto.Page;

import flexjson.JSONSerializer;

@RequestMapping("/metadata/security/group")
@Controller
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    GroupDao groupDao;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    public static class UpdateMembershipRequest {

        private Long userId;

        private Long groupId;

        private Boolean isMember;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public Boolean getIsMember() {
            return isMember;
        }

        public void setIsMember(Boolean isMember) {
            this.isMember = isMember;
        }

    }

    @Transactional
    @RequestMapping(method = POST)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_ADMIN")
    public Group create(@RequestBody Group group) throws Exception {
        groupDao.persist(group);
        return group;
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = { POST, PUT })
    @ResponseBody
    @JsonView(View.DetailWithUsers.class)
    @Secured("ROLE_ADMIN")
    public Group update(@RequestBody Group group) throws Exception {
        Group resultGroup = groupDao.merge(group);
        return resultGroup;
    }

    @RequestMapping(method = GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    @JsonView(View.Summary.class)
    public ResponseEntity<String> list() {
        List<Group> groups = groupDao.findAll();

        return new ResponseEntity<String>(new JSONSerializer().include("id").include("name").include("roles.id")
                .include("users.id").exclude("*").serialize(groups), new HttpHeaders(), OK);
    }

    @RequestMapping(path = "/user", method = PUT)
    @JsonView(View.Summary.class)
    @Transactional
    @ResponseStatus(OK)
    @Secured("ROLE_ADMIN")
    public void updateMembership(@RequestBody UpdateMembershipRequest req) {
        groupService.updateMembership(req.getUserId(), req.getGroupId(), req.getIsMember());
    }

    @RequestMapping(path = "/user/filter", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_ADMIN")
    public Page<GroupMemberDto> filter(@RequestParam(name = "userId", required = true) Long userId,
            @RequestParam(name = "fltrName", required = false) String fltrName,
            @RequestParam(name = "fltrRole", required = false) String fltrRole,
            @RequestParam(name = "fltrIsMember", required = false) Boolean fltrIsMemeber,
            @RequestParam(name = "sortProperty", required = false) String sortProperty,
            @RequestParam(name = "sortOrder", required = false) String sortOrder,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit) {
        return groupService.filter(userId, fltrName, fltrRole, fltrIsMemeber, sortProperty, sortOrder, offset, limit);
    }

    @RequestMapping(value = "/roles", method = GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    @JsonView(View.Summary.class)
    public ResponseEntity<String> listRoles() {
        List<Role> roles = roleDao.findAll();

        return new ResponseEntity<String>(
                new JSONSerializer().include("id").include("display").exclude("*").serialize(roles), new HttpHeaders(),
                OK);
    }

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    @JsonView(View.DetailWithUsers.class)
    public ResponseEntity<String> get(@PathVariable("id") Long id) {
        Group group = groupDao.findOne(id);
        group.getUsers().size();
        return new ResponseEntity<String>(group.toJson(), new HttpHeaders(), OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}/role/{roleId}", method = POST)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void addRole(@PathVariable("id") Long id, @PathVariable("roleId") Long roleId) throws Exception {
        Group group = groupDao.findOne(id);
        Role role = roleDao.findOne(roleId);
        group.getRoles().add(role);
        groupDao.merge(group);
    }

    @Transactional
    @RequestMapping(value = "/{id}/role/{roleId}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void removeRole(@PathVariable("id") Long id, @PathVariable("roleId") Long roleId) throws Exception {
        Group group = groupDao.findOne(id);
        Role role = roleDao.findOne(roleId);
        group.getRoles().remove(role);
        groupDao.merge(group);
    }

    @Transactional
    @RequestMapping(value = "/{id}/user/{userId}", method = POST)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void addUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) throws Exception {
        Group group = groupDao.findOne(id);
        User user = userDao.findOne(userId);
        group.getUsers().add(user);
        groupDao.merge(group);
    }

    @Transactional
    @RequestMapping(value = "/{id}/user/{roleId}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void removeUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) throws Exception {
        Group group = groupDao.findOne(id);
        User user = userDao.findOne(userId);
        group.getUsers().remove(user);
        groupDao.merge(group);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void delete(@PathVariable("id") Long id) throws Exception {
        Group group = groupDao.findOne(id);
        groupDao.remove(group);
    }
}
