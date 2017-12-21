package com.turbointernational.metadata.web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytroy.trunykov@zorallabs.com
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Aggregation {

    @JsonView({ View.Summary.class })
    private final String name;

    @JsonView({ View.Summary.class })
    private final List<Bucket> buckets;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class Bucket {

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

    public Aggregation(String name, List<Bucket> buckets) {
        this.name = name;
        this.buckets = buckets;
    }

    public String getName() {
        return name;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

}
