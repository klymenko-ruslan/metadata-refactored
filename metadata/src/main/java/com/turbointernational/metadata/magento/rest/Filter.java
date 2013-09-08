package com.turbointernational.metadata.magento.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.sf.jsog.client.UrlBuilder;

/**
 * @see http://www.magentocommerce.com/api/rest/get_filters.html
 */
public class Filter {

    public Filter(String attribute, FilterType type) {
        this.attribute = attribute;
        this.type = type;
    }
    
    private String attribute;
    
    private FilterType type;
    
    private List<String> values = new LinkedList();
    
    public Filter addParameter(String... values) {
        this.values.addAll(Arrays.asList(values));
        return this;
    }

    public void addToUrl(int index, UrlBuilder url) {
        
        // Build the filter prefix and add the attribute
        String filterPrefix = "filter[" + index + "]";
        url.set(filterPrefix + "[attribute]", attribute);
            
        // If there's more than one value, we need to index them
        if (values.size() == 1) {
            
            // Just the one value
            String value = values.iterator().next();
            url.set(filterPrefix + "[" + type + "]", value);
        } else {
            
            // Multiple values, add indexes
            int valueIndex = 0;
            for (String value : values) {
                url.set(filterPrefix + "[" + type + "]"+ "[" + valueIndex + "]", value);
                valueIndex++;
            }
        }
    }
}
