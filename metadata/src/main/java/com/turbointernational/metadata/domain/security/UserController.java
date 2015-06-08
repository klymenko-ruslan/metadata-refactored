package com.turbointernational.metadata.domain.security;
import com.google.common.collect.Sets;
import flexjson.JSONSerializer;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/security/user")
@Controller
public class UserController {
    
    @Autowired(required=true)
    UserDao userDao;
    
    @Autowired(required=true)
    GroupDao groupDao;

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity getMe() {
        User user = User.getCurrentUser();
        
        if (user == null) {
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
        
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> authoritySet = Sets.newTreeSet();
        
        for (GrantedAuthority auth: authentication.getAuthorities()) {
            authoritySet.add(auth.getAuthority());
        }
        
        return new ResponseEntity<String>(
                User.JSON.serialize(user),
                new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/me", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> updateMe(@RequestBody String json) {
        User jsonUser = User.fromJson(json);
        
        User user = User.getCurrentUser();
        user.setName(jsonUser.getName());
        user.setEmail(jsonUser.getEmail());
        
        // Password
        if (StringUtils.isNotBlank(jsonUser.getPassword())) {
            user.setPassword(BCrypt.hashpw(jsonUser.getPassword(), BCrypt.gensalt()));
        }
        
        userDao.merge(user);
        
        return new ResponseEntity<String>(
                User.JSON.serialize(user),
                new HttpHeaders(), HttpStatus.OK);
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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> list() {
        List<User> users = userDao.findActiveUsers();
        
        return new ResponseEntity<String>(
                User.JSON.serialize(users),
                new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> get(@PathVariable("id") Long id) {
        User user = userDao.findOne(id);
        
        return new ResponseEntity<String>(
                User.JSON.serialize(user),
                new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void update(@PathVariable("id") Long id, @RequestBody String json) {
        User jsonUser = User.fromJson(json);
        
        User user = userDao.findOne(id);
        user.setName(jsonUser.getName());
        user.setEmail(jsonUser.getEmail());
        
        // Password
        if (StringUtils.isNotBlank(jsonUser.getPassword())) {
            user.setPassword(BCrypt.hashpw(jsonUser.getPassword(), BCrypt.gensalt()));
        }
        
        userDao.merge(user);
    }
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> create(@RequestBody String json) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Create the object
        User user = User.fromJson(json);
        
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        
        userDao.persist(user);
        
        return new ResponseEntity<String>(user.toJson(), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void delete(@PathVariable("id") Long id) throws Exception {
        User user = userDao.findOne(id);
        user.setEnabled(false);
        userDao.merge(user);
    }
}
