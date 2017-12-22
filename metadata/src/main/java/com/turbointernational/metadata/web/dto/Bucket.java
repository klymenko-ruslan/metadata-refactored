package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytroy.trunykov@zorallabs.com
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Bucket {

    @JsonView({ View.Summary.class })
    private final Object key;

    @JsonView({ View.Summary.class })
    private final long count;

    public Bucket(Object key, long count) {
        this.key = key;
        this.count = count;
    }

    public Object getKey() {
        return key;
    }

    public long getCount() {
        return count;
    }

}
