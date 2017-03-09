package com.turbointernational.metadata.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import com.turbointernational.metadata.dao.GroupDao;
import com.turbointernational.metadata.dao.UserDao;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Page;

import flexjson.JSONSerializer;

@RequestMapping("/metadata/security/user")
@Controller
public class UserController {

    @Autowired
    UserDao userDao;

    @Autowired
    GroupDao groupDao;

    @JsonView(View.DetailWithGroups.class)
    @RequestMapping(value = "/me", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public User getMe() {
        return User.getCurrentUser();
    }

    @ResponseBody
    @Transactional
    @Secured("ROLE_READ")
    @JsonView(View.DetailWithGroups.class)
    @RequestMapping(value = "/me", method = POST)
    public User updateMe(@RequestBody User jsonUser) {

        // Manually copy the properties we're interested in
        User user = User.getCurrentUser();
        user.setName(jsonUser.getName());
        user.setEmail(jsonUser.getEmail());

        // Password
        if (StringUtils.isNotBlank(jsonUser.getPassword())) {
            user.setPassword(BCrypt.hashpw(jsonUser.getPassword(), BCrypt.gensalt()));
        }

        userDao.merge(user);

        return user;
    }

    @RequestMapping(value = "/myroles", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> myroles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> authoritySet = Sets.newTreeSet();

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            authoritySet.add(auth.getAuthority());
        }

        return new ResponseEntity<String>(new JSONSerializer().serialize(authoritySet), new HttpHeaders(),
                HttpStatus.OK);
    }

    @JsonView(View.SummaryWithGroups.class)
    @RequestMapping(method = GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public List<User> findActiveUsers() {
        return userDao.findActiveUsers();
    }

    @JsonView(View.SummaryWithGroups.class)
    @RequestMapping(value = "list", method = GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    @JsonView(View.SummaryWithGroups.class)
    @RequestMapping(value = "/filter", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Page<User> filterUsers(@RequestParam(name = "displayName", required = false) String displayName,
            @RequestParam(name = "userName", required = false) String userName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "authProviderId", required = false) Long authProviderId,
            @RequestParam(name = "enabled", required = false) Boolean enabled,
            @RequestParam(name = "sortProperty", required = false) String sortProperty,
            @RequestParam(name = "sortOrder", required = false) String sortOrder,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit) {
        return userDao.filterUsers(displayName, userName, email, authProviderId, enabled,
                sortProperty, sortOrder, offset, limit);
    }

    @JsonView(View.DetailWithGroups.class)
    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public User get(@PathVariable("id") Long id) {
        return userDao.findOne(id);
    }

    @ResponseBody
    @Secured("ROLE_ADMIN")
    @Transactional
    @RequestMapping(value = "/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public void update(@PathVariable("id") Long id, @RequestBody User jsonUser) {
        User user = userDao.findOne(id);
        user.setName(jsonUser.getName());
        user.setUsername(jsonUser.getUsername());
        user.setEmail(jsonUser.getEmail());
        // Password
        if (StringUtils.isNotBlank(jsonUser.getPassword())) {
            user.setPassword(BCrypt.hashpw(jsonUser.getPassword(), BCrypt.gensalt()));
        }
        if (jsonUser.getAuthProvider().getId() < 0) {
            user.setAuthProvider(null);
        } else {
            user.setAuthProvider(jsonUser.getAuthProvider());
        }
        userDao.merge(user);
    }

    @ResponseBody
    @Transactional
    @Secured("ROLE_ADMIN")
    @JsonView(View.Detail.class)
    @RequestMapping(method = POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody User user) throws Exception {
        if (user.getAuthProvider().getId() < 0) {
            user.setAuthProvider(null);
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }
        userDao.persist(user);
        return user;
    }

    @ResponseBody
    @Transactional
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        User user = userDao.findOne(id);
        user.setEnabled(false);
        userDao.merge(user);
    }
}
