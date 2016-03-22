package com.turbointernational.metadata.domain.security;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 18.03.16.
 */
@Entity
@PrimaryKeyJoinColumn(name = "id")
@NamedQueries(
        @NamedQuery(name = "findAuthProviderLdapByName", query = "from AuthProviderLdap where name=:name")
)
public class AuthProviderLdap extends AuthProvider {

    public enum ProtocolEnum {LDAP, LDAPS, LDAPS_SOFT}

    ;

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Column(nullable = false, unique = true)
    @JsonView({View.Summary.class})
    private String name;

    @Column(nullable = false)
    @JsonView({View.Summary.class})
    private String host;

    @Column(nullable = false)
    @JsonView({View.Summary.class})
    private int port;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView({View.Summary.class})
    private ProtocolEnum protocol;

    //</editor-fold>

    public AuthProviderLdap() {
        super();
        setTyp(AuthProviderTypeEnum.LDAP);
    }

    public AuthProviderLdap(String name, String host, int port) {
        super();
        this.name = name;
        this.host = host;
        this.port = port;
        setTyp(AuthProviderTypeEnum.LDAP);
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

    public ProtocolEnum getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolEnum protocol) {
        this.protocol = protocol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AuthProviderLdap that = (AuthProviderLdap) o;

        if (port != that.port) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        return protocol == that.protocol;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
        return result;
    }

}
