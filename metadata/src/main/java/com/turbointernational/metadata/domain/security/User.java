package com.turbointernational.metadata.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import com.turbointernational.metadata.web.View;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

/*

alter table user add unique key `name` (`name`);
create table auth_provider(id int primary key, typ enum('LDAP') not null);
create table auth_provider_ldap(
    id int primary key references auth_provider(id) on update cascade on delete cascade,
    name varchar(64) not null,
    host varchar(255) not null,
    port int not null default 636,
    unique key name (name)
);
alter table user add column auth_provider_id int default null references auth_provider(id) on update cascade on delete no action;
*/

@Entity
@Table(name="USER")
@NamedQueries({
    @NamedQuery(name = "findUserByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "findUserByName", query = "SELECT u FROM User u WHERE u.name = :name")
})
public class User implements Comparable<User>, UserDetails {

//    public static final JSONSerializer JSON = new JSONSerializer()
//                .include("id")
//                .include("name")
//                .include("email")
//                .include("enabled")
//                .include("groups.id")
//                .include("groups.name")
//                .include("roles")
//                .exclude("*");

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Detail.class, View.Summary.class})
    private Long id;
    
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(unique = true)
    private String name;
    
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(unique = true)
    private String email;
    
    private String password;
    
    @JsonIgnore
    private String passwordResetToken;
    
    @JsonView(View.Detail.class)
    @Column(columnDefinition = "BIT")
    private Boolean enabled;

    @OneToOne(fetch = FetchType.EAGER)
    private AuthProvider authProvider;
    
    @JsonView({View.SummaryWithGroups.class, View.DetailWithGroups.class})
    @ManyToMany(mappedBy="users", fetch = FetchType.EAGER)
    private Set<Group> groups = new TreeSet<>();
    
    // For UserDetails
    @JsonIgnore
    private final transient Set<SimpleGrantedAuthority> authorities = Sets.newHashSet();
    
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
    
    @Override
    @JsonIgnore
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return getEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return getEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return getEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return getEnabled();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Spring Security">
    public static User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof User) {
            return (User) principal;
        }
        
        return null;
    }
    //</editor-fold>
    
    @Override
    public int compareTo(User t) {
        return this.getName().compareTo(t.getName());
    }
}


