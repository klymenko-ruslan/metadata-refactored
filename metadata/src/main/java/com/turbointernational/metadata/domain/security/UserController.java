package com.turbointernational.metadata.domain.security;
import com.google.common.collect.Sets;
import flexjson.JSONSerializer;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/security/user/")
@Controller
public class UserController {

    @RequestMapping(value = "/myroles", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_AUTHENTICATED")
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
    @Secured("ROLE_AUTHENTICATED")
    public ResponseEntity<String> list() {
        List<User> users = User.findAllUsers();
        
        return new ResponseEntity<String>(
                User.JSON.serialize(users),
                new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_AUTHENTICATED")
    public ResponseEntity<String> get(@PathVariable("id") Long id) {
        User user = User.findUser(id);
        
        return new ResponseEntity<String>(
                new JSONSerializer().serialize(user.getGroups()),
                new HttpHeaders(), HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> create(@RequestBody String json) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Create the object
        User user = User.fromJson(json);
        user.persist();
        
        return new ResponseEntity<String>(user.toJson(), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value="/{id}/group/{groupId}", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void addGroup(@PathVariable("id") Long id, @PathVariable("groupId") Long groupId) throws Exception {
        User user = User.findUser(id);
        Group group = Group.findGroup(groupId);
        user.getGroups().add(group);
        user.merge();
    }
    
    @Transactional
    @RequestMapping(value="/{id}/group/{groupId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void removeGroup(@PathVariable("id") Long id, @PathVariable("groupId") Long groupId) throws Exception {
        User user = User.findUser(id);
        Group group = Group.findGroup(groupId);
        user.getGroups().remove(group);
        user.merge();
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void delete(@PathVariable("id") Long id) throws Exception {
        Group group = Group.findGroup(id);
        group.remove();
    }
}
