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
    public Page getAllAuthProviders(@RequestParam(name = "sortProperty") String sortProperty,
                                    @RequestParam(name = "sortOrder") String sortOrder,
                                    @RequestParam(name = "offset") int offset,
                                    @RequestParam(name = "limit") int limit) {
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

}
