package com.turbointernational.metadata.domain.security;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Configurable
@Entity
@RooJpaActiveRecord(table="USER")
public class User implements UserDetails {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String email;
    
    private String password;
    
    private String passwordSalt;
    
    @Column(columnDefinition = "BIT")
    private Boolean enabled;
    
    @ManyToMany
    @JoinTable(name="USER_GROUP",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="group_id"))
    private Set<Group> groups;
    
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPasswordSalt() {
        return passwordSalt;
    }
    
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public Set<Group> getGroups() {
        return groups;
    }
    
    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Active Record">
    public static User getByPrincipal(Principal principal) {
        // Get the user
        throw new UnsupportedOperationException();
    }
    
    public static User findUserByEmail(String email) {
        if (StringUtils.isBlank(email))
            return null;
        
        return (User) User.entityManager()
                .createQuery("SELECT u FROM User u WHERE u.email = ?")
                .setParameter(0, email)
                .getSingleResult();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Spring Security">
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new TreeSet();
        
        for (Group group : groups) {
            for (Role role : group.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        }
        
        return authorities;
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return getEnabled();
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return getEnabled();
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return getEnabled();
    }
    
    @Override
    public boolean isEnabled() {
        return getEnabled();
    }
    //</editor-fold>
}
