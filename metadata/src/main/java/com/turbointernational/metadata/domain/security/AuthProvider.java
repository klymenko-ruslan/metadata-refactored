package com.turbointernational.metadata.domain.security;

import javax.persistence.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 18.03.16.
 */
@Entity
public class AuthProvider {

    private enum AuthProviderTypeEnum{ ldap };

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
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
