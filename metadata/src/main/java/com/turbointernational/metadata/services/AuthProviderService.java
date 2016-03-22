package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.security.AuthProviderDao;
import com.turbointernational.metadata.domain.security.AuthProviderLdap;
import com.turbointernational.metadata.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dmytro.trunykov@zorallabs.com on 21.03.16.
 */
@Service
public class AuthProviderService {

    @Autowired
    private AuthProviderDao authProviderDao;

    @Transactional
    public Page getAllAuthProviders(String sortProperty, String sortOrder, int offset, int limit) {
        return authProviderDao.getAllAuthProviders(sortProperty, sortOrder, offset, limit);
    }

    @Transactional
    public Long createAuthProviderLDAP(AuthProviderLdap authProviderLDAP) {
        return authProviderDao.createAuthProviderLDAP(authProviderLDAP);
    }

    @Transactional
    public void updateAuthProviderLDAP(AuthProviderLdap authProviderLDAP) {
        authProviderDao.updateAuthProviderLDAP(authProviderLDAP);
    }

    @Transactional
    public void removeAuthProvider(Long id) {
        authProviderDao.removeAuthProvider(id);
    }

    @Transactional
    public AuthProviderLdap findAuthProviderLdapByName(String name) {
        return authProviderDao.findAuthProviderLdapByName(name);
    }

}
