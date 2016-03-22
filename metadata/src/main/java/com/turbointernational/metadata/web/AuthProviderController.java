package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.security.AuthProviderLdap;
import com.turbointernational.metadata.services.AuthProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    public Page getAllAuthProviders(@RequestParam("sortProperty") String sortProperty,
                                    @RequestParam("sortOrder") String sortOrder,
                                    @RequestParam("offset") int offset,
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
