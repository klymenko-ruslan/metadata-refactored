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

ALTER TABLE user ADD UNIQUE KEY `name` (`name`);

CREATE TABLE `auth_provider` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `typ` enum('LDAP') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB;

CREATE TABLE `auth_provider_ldap` (
  `id` bigint(20) NOT NULL,
  `name` varchar(64) NOT NULL,
  `host` varchar(255) NOT NULL,
  `port` int(11) NOT NULL DEFAULT '636',
  `protocol` enum('LDAP','LDAPS','LDAPS_SOFT') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  CONSTRAINT `auth_provider_ldap_ibfk_1` FOREIGN KEY (`id`) REFERENCES `auth_provider` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB;

ALTER TABLE user ADD COLUMN auth_provider_id bigint;
ALTER TABLE user ADD CONSTRAINT fk_authp FOREIGN KEY (auth_provider_id) REFERENCES auth_provider(id) ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE user ADD COLUMN logon VARCHAR(255) NULL UNIQUE;

alter table auth_provider_ldap add column domain varchar(255);

*/

@Entity
@Table(name="USER")
@NamedQueries({
    @NamedQuery(name = "findUserByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "findUserByLogon", query = "SELECT u FROM User u WHERE u.logon = :logon")
})
public class User implements Comparable<User>, UserDetails {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Detail.class, View.Summary.class})
    private Long id;
    
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(unique = true)
    private String name;

    /**
     * This name is used for authentication against database.
     */
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(unique = true)
    private String email;

    /**
     * This name is used for authentication against LDAP servers.
     */
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(unique = true)
    private String logon;

    private String password;
    
    @JsonIgnore
    private String passwordResetToken;
    
    @JsonView(View.Detail.class)
    @Column(columnDefinition = "BIT")
    private Boolean enabled;

    @JsonView(View.Detail.class)
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

    public String getLogon() {
        return logon;
    }

    public void setLogon(String logon) {
        this.logon = logon;
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
        if (authProvider == null) {
            return email; // localdb authentication
        } else if (authProvider.getTyp() == AuthProvider.AuthProviderTypeEnum.LDAP) {
            return logon;
        }
        throw new IllegalArgumentException("Unknown authentication provider: " + authProvider);
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


