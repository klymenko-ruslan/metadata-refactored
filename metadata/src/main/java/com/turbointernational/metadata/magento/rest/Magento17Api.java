/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turbointernational.metadata.magento.rest;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 *
 * @author jrodriguez
 */
public abstract class Magento17Api extends DefaultApi10a {
   
    public abstract String getBaseUrl();

    @Override
    public String getRequestTokenEndpoint() {
        return getBaseUrl() + "oauth/initiate";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return getBaseUrl() + "oauth/token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return getBaseUrl() + "admin/oauth_authorize?oauth_token=" + requestToken.getToken(); //this implementation is for admin roles only...
    }
}