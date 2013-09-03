package com.turbointernational.metadata.magento.rest;

import java.util.LinkedList;
import java.util.List;
import net.sf.jsog.JSOG;
import net.sf.jsog.client.UrlBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

/**
 *
 * @author jrodriguez
 */
public class MagentoRequest {
    private Verb verb;
    
    private String command;
    
    private UrlBuilder baseUrl;
    
    private List<Filter> filters = new LinkedList();
    
    private JSOG payload = null;

    public MagentoRequest(Verb verb, UrlBuilder baseUrl, String command) {
        this.verb = verb;
        this.baseUrl = baseUrl;
        this.command = command;
    }

    public void setPayload(JSOG payload) {
        this.payload = payload;
    }
    
    private String getUrl() {
        
        // Add the filters
        for (int i = 0; i < filters.size(); i++) {
            filters.get(i).addToUrl(i, baseUrl);
        }

        // Return the URL with the command and parameters included
        return baseUrl.toString(command);
    }
    
    public OAuthRequest getOAuthRequest() {
        
        // Build the basic request
        OAuthRequest request = new OAuthRequest(verb, getUrl());
        
        // Add the payload, if any
        if (payload != null) {
            request.addPayload(payload.toString());
        }
        
        return request;
    }
    
    public Filter addFilter(String attribute, FilterType type) {
        Filter filter = new Filter(attribute, type);
        
        filters.add(filter);
        
        return filter;
    }
    
}
