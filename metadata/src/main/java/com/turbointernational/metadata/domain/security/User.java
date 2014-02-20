package com.turbointernational.metadata.domain.security;
import static com.turbointernational.metadata.domain.part.Part.entityManager;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Configurable
@Entity
@Table(name="USER")
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
        if (StringUtils.isNotBlank(email)) {
            List<User> users = User.entityManager()
                    .createQuery("SELECT u FROM User u WHERE u.email = ?")
                    .setParameter(1, email)
                    .getResultList();
            
            if (!users.isEmpty()) {
                return users.get(0);
            }
        }
        
        return null;
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
        
    @PersistenceContext
    @Transient
    private transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new User().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countUsers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM User o", Long.class).getSingleResult();
    }
    
    public static List<User> findAllUsers() {
        return entityManager().createQuery("SELECT o FROM User o", User.class).getResultList();
    }
    
    public static User findUser(Long id) {
        if (id == null) return null;
        return entityManager().find(User.class, id);
    }
    
    public static List<User> findUserEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM User o", User.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            User attached = findUser(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public User merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        User merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    //</editor-fold>
}
