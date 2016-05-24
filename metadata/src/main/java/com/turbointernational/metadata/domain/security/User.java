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

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name="user")
@NamedQueries({
    @NamedQuery(name = "findUserByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "findUserByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User implements Comparable<User>, UserDetails {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Detail.class, View.Summary.class})
    private Long id;
    
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(name = "name", unique = true)
    private String name;

    @JsonView({View.Detail.class, View.Summary.class})
    @Column(name = "email", unique = true)
    private String email;

    /**
     * This field is used for authentication.
     */
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;
    
    @JsonIgnore
    @Column(name = "password_reset_token")
    private String passwordResetToken;
    
    @JsonView(View.Detail.class)
    @Column(name = "enabled", columnDefinition = "BIT")
    private Boolean enabled;

    @JsonView(View.Summary.class)
    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "auth_provider_id")
    private AuthProvider authProvider;
    
    @JsonView({View.SummaryWithGroups.class, View.DetailWithGroups.class})
    @ManyToMany(mappedBy="users", fetch = EAGER)
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

    public void setUsername(String username) {
        this.username = username;
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
        return username;
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


