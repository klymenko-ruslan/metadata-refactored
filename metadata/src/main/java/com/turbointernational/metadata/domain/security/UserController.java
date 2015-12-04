package com.turbointernational.metadata.domain.security;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/metadata/security/user")
@Controller
public class UserController {
    
    @Autowired(required=true)
    UserDao userDao;
    
    @Autowired(required=true)
    GroupDao groupDao;

    @JsonView(View.DetailWithGroups.class)
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public User getMe() {
        return User.getCurrentUser();
    }

    @ResponseBody
    @Transactional
    @Secured("ROLE_READ")
    @JsonView(View.DetailWithGroups.class)
    @RequestMapping(value = "/me", method = RequestMethod.POST)
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

    @RequestMapping(value = "/myroles", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> myroles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> authoritySet = Sets.newTreeSet();
        
        for (GrantedAuthority auth: authentication.getAuthorities()) {
            authoritySet.add(auth.getAuthority());
        }
        
        return new ResponseEntity<String>(
                new JSONSerializer().serialize(authoritySet),
                new HttpHeaders(), HttpStatus.OK);
    }

    @JsonView(View.SummaryWithGroups.class)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public List<User> list() {
        return userDao.findActiveUsers();
    }

    @JsonView(View.DetailWithGroups.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public User get(@PathVariable("id") Long id) {
        return userDao.findOne(id);
    }

    @ResponseBody
    @Secured("ROLE_ADMIN")
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable("id") Long id, @RequestBody User jsonUser) {
        
        User user = userDao.findOne(id);
        user.setName(jsonUser.getName());
        user.setEmail(jsonUser.getEmail());
        
        // Password
        if (StringUtils.isNotBlank(jsonUser.getPassword())) {
            user.setPassword(BCrypt.hashpw(jsonUser.getPassword(), BCrypt.gensalt()));
        }
        
        userDao.merge(user);
    }
    
    @ResponseBody
    @Transactional
    @Secured("ROLE_ADMIN")
    @JsonView(View.Detail.class)
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody User user) throws Exception {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        
        userDao.persist(user);
        
        return user;
    }
    
    @ResponseBody
    @Transactional
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        User user = userDao.findOne(id);
        user.setEnabled(false);
        userDao.merge(user);
    }
}
