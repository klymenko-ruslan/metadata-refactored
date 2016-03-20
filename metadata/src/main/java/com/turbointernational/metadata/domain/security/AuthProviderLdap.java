package com.turbointernational.metadata.domain.security;

import javax.persistence.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 18.03.16.
 */
@Entity
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("LDAP")
public class AuthProviderLdap extends AuthProvider {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private int port;

    //</editor-fold>

    public AuthProviderLdap() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthProviderLdap)) return false;
        if (!super.equals(o)) return false;

        AuthProviderLdap that = (AuthProviderLdap) o;

        if (port != that.port) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return host != null ? host.equals(that.host) : that.host == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }

}
