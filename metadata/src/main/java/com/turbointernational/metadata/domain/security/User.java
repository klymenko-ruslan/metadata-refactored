package com.turbointernational.metadata.domain.security;
import com.google.common.collect.Sets;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="USER")
public class User implements Comparable<User>, UserDetails {
    public static final long serialVersionUID = 1L;
    
    public static final JSONSerializer JSON = new JSONSerializer()
                .include("id")
                .include("name")
                .include("email")
                .include("enabled")
                .include("groups.id")
                .include("groups.name")
                .include("roles")
                .exclude("*");

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String email;
    
    private String password;
    
    private String passwordResetToken;
    
    @Column(columnDefinition = "BIT")
    private Boolean enabled;
    
    @ManyToMany(mappedBy="users", fetch = FetchType.EAGER)
    private Set<Group> groups = new TreeSet<Group>();
    
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
    
    public Set<Group> getGroups() {
        return groups;
    }
    
    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return JSON.serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static User fromJson(String json) {
        return new JSONDeserializer<User>().use(null, User.class).deserialize(json);
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
    
    public Set<String> getRoles() {
        Set<String> authorities = Sets.newTreeSet();
        
        for (Group group : groups) {
            for (Role role : group.getRoles()) {
                authorities.add(role.getName());
            }
        }
        
        return authorities;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = Sets.newHashSet();
        
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
    
    @Override
    public int compareTo(User t) {
        return this.getName().compareTo(t.getName());
    }
}


