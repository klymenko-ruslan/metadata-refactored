package com.turbointernational.metadata.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Table(name="groups") // GROUP is a reserved word
public class Group implements Comparable<Group>, Serializable {

    public static final long serialVersionUID = 1L;

    public static JSONSerializer JSON = new JSONSerializer()
                .include("id")
                .include("name")
                .include("roles.id")
                .include("roles.display")
                .include("users.id")
                .include("users.name")
                .include("users.email")
                .exclude("*");

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Detail.class, View.Summary.class})
    private Long id;
    
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(name = "name")
    private String name;
    
    @ManyToMany
    @JoinTable(name="user_group",
            joinColumns=@JoinColumn(name="group_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    @JsonView({View.DetailWithUsers.class})
    private Set<User> users = new TreeSet<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="group_role",
            joinColumns=@JoinColumn(name="group_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    @JsonView({View.Detail.class})
    private Set<Role> roles = new TreeSet<>();
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Set<User> getUsers() {
        return users;
    }
    
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return JSON.serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static Group fromJson(String json) {
        return new JSONDeserializer<Group>().use(null, Group.class).deserialize(json);
    }
    //</editor-fold>
    
    @Override
    public int compareTo(Group t) {
        return this.getName().compareTo(t.getName());
    }
    
}
