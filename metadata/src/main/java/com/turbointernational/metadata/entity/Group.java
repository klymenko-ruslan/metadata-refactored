package com.turbointernational.metadata.entity;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Table(name = "groups") // GROUP is a reserved word
public class Group implements Comparable<Group>, Serializable {

    private static final long serialVersionUID = 3709693991163547375L;

    public static JSONSerializer JSON = new JSONSerializer().include("id").include("name").include("roles.id")
            .include("roles.display").include("users.id").include("users.name").include("users.email").exclude("*");

    // <editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView({ View.Detail.class, View.Summary.class })
    private Long id;

    @JsonView({ View.Detail.class, View.Summary.class })
    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonView({ View.DetailWithUsers.class })
    private Set<User> users = new TreeSet<>();

    @OneToMany(mappedBy = "group", fetch = LAZY)
    private List<UserGroup> userGroups = new ArrayList<>();

    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "group_role", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonView({ View.Detail.class })
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

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return JSON.serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static Group fromJson(String json) {
        return new JSONDeserializer<Group>().use(null, Group.class).deserialize(json);
    }
    // </editor-fold>

    @Override
    public int compareTo(Group t) {
        return this.getName().compareTo(t.getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
