package com.turbointernational.metadata.domain.security;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/security/group")
@Controller
public class GroupController {
    
    @Autowired(required=true)
    GroupDao groupDao;
    
    @Autowired(required=true)
    UserDao userDao;
    
    @Autowired(required=true)
    RoleDao roleDao;
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_ADMIN")
    public Group create(@RequestBody Group group) throws Exception {
        groupDao.persist(group);
        return group;
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    @JsonView(View.DetailWithUsers.class)
    @Secured("ROLE_ADMIN")
    public Group update(@RequestBody Group group) throws Exception {
        Group resultGroup = groupDao.merge(group);
        return resultGroup;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    @JsonView(View.Summary.class)
    public ResponseEntity<String> list() {
        List<Group> groups = groupDao.findAll();
        
        return new ResponseEntity<String>(
            new JSONSerializer()
                .include("id")
                .include("name")
                .include("roles.id")
                .include("users.id")
                .exclude("*")
                .serialize(groups),
                new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    @JsonView(View.Summary.class)
    public ResponseEntity<String> listRoles() {
        List<Role> roles = roleDao.findAll();
        
        return new ResponseEntity<String>(
            new JSONSerializer()
                .include("id")
                .include("display")
                .exclude("*")
                .serialize(roles),
                new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    @JsonView(View.DetailWithUsers.class)
    public ResponseEntity<String> get(@PathVariable("id") Long id) {
        Group group = groupDao.findOne(id);
        group.getUsers().size();
        return new ResponseEntity<String>(group.toJson(), new HttpHeaders(), HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value="/{id}/role/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void addRole(@PathVariable("id") Long id, @PathVariable("roleId") Long roleId) throws Exception {
        Group group = groupDao.findOne(id);
        Role role = roleDao.findOne(roleId);
        group.getRoles().add(role);
        groupDao.merge(group);
    }
    
    @Transactional
    @RequestMapping(value="/{id}/role/{roleId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void removeRole(@PathVariable("id") Long id, @PathVariable("roleId") Long roleId) throws Exception {
        Group group = groupDao.findOne(id);
        Role role = roleDao.findOne(roleId);
        group.getRoles().remove(role);
        groupDao.merge(group);
    }
    
    @Transactional
    @RequestMapping(value="/{id}/user/{userId}", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void addUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) throws Exception {
        Group group = groupDao.findOne(id);
        User user = userDao.findOne(userId);
        group.getUsers().add(user);
        groupDao.merge(group);
    }
    
    @Transactional
    @RequestMapping(value="/{id}/user/{roleId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void removeUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) throws Exception {
        Group group = groupDao.findOne(id);
        User user = userDao.findOne(userId);
        group.getUsers().remove(user);
        groupDao.merge(group);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void delete(@PathVariable("id") Long id) throws Exception {
        Group group = groupDao.findOne(id);
        groupDao.remove(group);
    }
}
