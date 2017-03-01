package com.turbointernational.metadata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 18.03.16.
 */
@Entity
@Table(name = "auth_provider")
@Inheritance(strategy = InheritanceType.JOINED)
public class AuthProvider implements Serializable {

    private static final long serialVersionUID = -1177278382277025727L;

    public enum AuthProviderTypeEnum {LDAP}

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;

    @Column(name = "typ")
    @Enumerated(EnumType.STRING)
    @JsonView({View.Summary.class})
    private AuthProviderTypeEnum typ;
    //</editor-fold>

    public AuthProvider() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthProviderTypeEnum getTyp() {
        return typ;
    }

    public void setTyp(AuthProviderTypeEnum typ) {
        this.typ = typ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthProvider that = (AuthProvider) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return typ == that.typ;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (typ != null ? typ.hashCode() : 0);
        return result;
    }

}
