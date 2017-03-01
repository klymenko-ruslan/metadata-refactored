package com.turbointernational.metadata.entity;

import static com.turbointernational.metadata.entity.AuthProvider.AuthProviderTypeEnum.LDAP;
import static javax.persistence.EnumType.STRING;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 18.03.16.
 */
@Entity
@Table(name = "auth_provider_ldap")
@PrimaryKeyJoinColumn(name = "id")
@NamedQueries(
        @NamedQuery(name = "findAuthProviderLdapByName", query = "from AuthProviderLdap where name=:name")
)
public class AuthProviderLdap extends AuthProvider {

    private static final long serialVersionUID = -4541480301268203966L;

    public enum ProtocolEnum {LDAP, LDAPS, LDAPS_SOFT}

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Column(name = "name", nullable = false, unique = true)
    @JsonView({View.Summary.class})
    private String name;

    @Column(name = "host", nullable = false)
    @JsonView({View.Summary.class})
    private String host;

    @Column(name = "port", nullable = false)
    @JsonView({View.Summary.class})
    private int port;

    @Column(name = "protocol", nullable = false)
    @Enumerated(STRING)
    @JsonView({View.Summary.class})
    private ProtocolEnum protocol;

    @Column(name = "domain")
    @JsonView({View.Summary.class})
    private String domain;

    //</editor-fold>

    public AuthProviderLdap() {
        this(null, null, 0, null);
    }

    public AuthProviderLdap(String name, String host, int port, String domain) {
        super();
        this.name = name;
        this.host = host;
        this.port = port;
        this.domain = domain;
        setTyp(LDAP);
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
        if (protocol != that.protocol) return false;
        return domain != null ? domain.equals(that.domain) : that.domain == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

}
