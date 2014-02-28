package com.turbointernational.metadata.domain.security;
import com.turbointernational.metadata.domain.part.Interchange;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name="GROUPS") // GROUP is a reserved word
public class Group implements Comparable<Group> {
    public static JSONSerializer JSON = new JSONSerializer()
                .include("roles.id")
                .include("roles.name")
                .include("roles.display")
                .include("users.id")
                .include("users.name")
                .include("users.email")
                .exclude("*.class");

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToMany
    @JoinTable(name="USER_GROUP",
            joinColumns=@JoinColumn(name="group_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<User> users = new TreeSet<User>();
    
    @ManyToMany
    @JoinTable(name="GROUP_ROLE",
            joinColumns=@JoinColumn(name="group_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles = new TreeSet<Role>();
    
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
    
    public Set<User> getUsers() {
        return users;
    }
    
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="activerecord">
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Group().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countGroups() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Group o", Long.class).getSingleResult();
    }
    
    public static List<Group> findAllGroups() {
        return entityManager().createQuery("SELECT o FROM Group o", Group.class).getResultList();
    }
    
    public static Group findGroup(Long id) {
        if (id == null) return null;
        return entityManager().find(Group.class, id);
    }
    
    public static List<Group> findGroupEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Group o", Group.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Group attached = findGroup(this.id);
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
    public Group merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Group merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return JSON.serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static Group fromJson(String json) {
        return new JSONDeserializer<Group>().use(null, Group.class).deserialize(json);
    }
    //</editor-fold>
    
    @Override
    public int compareTo(Group t) {
        return this.getName().compareTo(t.getName());
    }
    
}
