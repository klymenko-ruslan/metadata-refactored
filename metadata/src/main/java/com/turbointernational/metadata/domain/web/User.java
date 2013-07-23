package com.turbointernational.metadata.domain.web;
import java.util.Set;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class User {
    private String name;

    private String email;

    private String password;

    private Boolean enabled;

    @ManyToMany
    @JoinTable(name="USER_GROUP",
               joinColumns=@JoinColumn(name="user_id"),
               inverseJoinColumns=@JoinColumn(name="group_id"))
    private Set<Group> groups;
}
