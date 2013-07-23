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
public class Group {
    private String name;

    @ManyToMany
    @JoinTable(name="USER_GROUP",
               joinColumns=@JoinColumn(name="group_id"),
               inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<User> users;

    @ManyToMany
    @JoinTable(name="GROUP_ROLE",
               joinColumns=@JoinColumn(name="group_id"),
               inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles;
}
