package com.turbointernational.metadata.domain.security;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role implements Comparable<Role>, Serializable {
    public static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Detail.class, View.Summary.class})
    private Long id;
    
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(name = "name")
    private String name;
    
    @JsonView({View.Detail.class, View.Summary.class})
    @Column(name = "display")
    private String display;
    
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
    
    public String getDisplay() {
        return display;
    }
    
    public void setDisplay(String display) {
        this.display = display;
    }
    //</editor-fold>
    
    @Override
    public int compareTo(Role t) {
        return this.getName().compareTo(t.getName());
    }
}
