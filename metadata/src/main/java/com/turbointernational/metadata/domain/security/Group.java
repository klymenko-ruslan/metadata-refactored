package com.turbointernational.metadata.domain.security;
import java.util.Set;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="GROUP")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
