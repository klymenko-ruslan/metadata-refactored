// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.security;

import com.turbointernational.metadata.domain.security.Group;
import com.turbointernational.metadata.domain.security.User;
import java.util.Set;

privileged aspect User_Roo_JavaBean {
    
    public Long User.getId() {
        return this.id;
    }
    
    public void User.setId(Long id) {
        this.id = id;
    }
    
    public String User.getName() {
        return this.name;
    }
    
    public void User.setName(String name) {
        this.name = name;
    }
    
    public String User.getEmail() {
        return this.email;
    }
    
    public void User.setEmail(String email) {
        this.email = email;
    }
    
    public String User.getPassword() {
        return this.password;
    }
    
    public void User.setPassword(String password) {
        this.password = password;
    }
    
    public Boolean User.getEnabled() {
        return this.enabled;
    }
    
    public void User.setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public Set<Group> User.getGroups() {
        return this.groups;
    }
    
    public void User.setGroups(Set<Group> groups) {
        this.groups = groups;
    }
    
}
