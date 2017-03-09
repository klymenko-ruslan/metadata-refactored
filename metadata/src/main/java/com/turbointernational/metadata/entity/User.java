package com.turbointernational.metadata.entity;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.FetchType.EAGER;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import com.turbointernational.metadata.util.View;

@Entity
@Table(name = "user")
@NamedQueries({ @NamedQuery(name = "findUserByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "findUserByUsername", query = "SELECT u FROM User u WHERE u.username = :username") })
@JsonInclude(ALWAYS)
public class User implements Comparable<User>, UserDetails {

    private static final long serialVersionUID = -6103427720512921025L;

    public final static Long SYNC_AGENT_USER_ID = 10000L;

    // <editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ View.Detail.class, View.Summary.class })
    private Long id;

    @JsonView({ View.Detail.class, View.Summary.class })
    @Column(name = "name", unique = true)
    private String name;

    @JsonView({ View.Detail.class, View.Summary.class })
    @Column(name = "email", unique = true)
    private String email;

    /**
     * This field is used for authentication.
     */
    @JsonView({ View.Detail.class, View.Summary.class })
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @JsonView(View.Summary.class)
    @Column(name = "enabled", columnDefinition = "BIT")
    private Boolean enabled;

    @JsonView(View.Summary.class)
    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "auth_provider_id")
    private AuthProvider authProvider;

    @JsonView({ View.SummaryWithGroups.class, View.DetailWithGroups.class })
    @ManyToMany(mappedBy = "users", fetch = EAGER)
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

    @Override
    public boolean isEnabled() {
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
        return isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Spring Security">
    public static User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }

        return null;
    }
    // </editor-fold>

    @Override
    public int compareTo(User t) {
        return this.getName().compareTo(t.getName());
    }
}
