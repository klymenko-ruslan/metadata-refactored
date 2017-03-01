package com.turbointernational.metadata.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.AuthProviderLdap;
import com.turbointernational.metadata.service.AuthProviderService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Page;

/**
 * Created by dmytro.trunykov@zorallabs.com on 21.03.16.
 */
@Controller
@RequestMapping("/metadata/authprovider")
public class AuthProviderController {

    @Autowired
    private AuthProviderService authProviderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    @JsonView(View.Summary.class)
    @Secured("ROLE_ADMIN")
    public Page<AuthProviderLdap> getAllAuthProviders(@RequestParam("sortProperty") String sortProperty,
            @RequestParam("sortOrder") String sortOrder, @RequestParam("offset") int offset,
            @RequestParam("limit") int limit) {
        return authProviderService.getAllAuthProviders(sortProperty, sortOrder, offset, limit);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @JsonView(View.Summary.class)
    @Secured("ROLE_ADMIN")
    public Long createAuthProviderLDAP(@RequestBody AuthProviderLdap authProviderLDAP) {
        return authProviderService.createAuthProviderLDAP(authProviderLDAP);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    @JsonView(View.Summary.class)
    @Secured("ROLE_ADMIN")
    public void updateAuthProviderLDAP(@RequestBody AuthProviderLdap authProviderLDAP) {
        authProviderService.updateAuthProviderLDAP(authProviderLDAP);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void removeAuthProvider(@PathVariable("id") Long id) {
        authProviderService.removeAuthProvider(id);
    }

    @Transactional
    @RequestMapping(value = "findbyname", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public AuthProviderLdap findAuthProviderLdapByName(@RequestParam("name") String name) {
        AuthProviderLdap retVal = authProviderService.findAuthProviderLdapByName(name);
        return retVal;
    }

}
