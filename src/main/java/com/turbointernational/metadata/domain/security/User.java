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
@RooJpaActiveRecord(table="USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
