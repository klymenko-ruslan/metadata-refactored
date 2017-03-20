package com.turbointernational.metadata.web.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 21.03.16.
 */
@JsonInclude(ALWAYS)
public class Page<T> {

    @JsonView({View.Summary.class})
    private final long total;

    @JsonView({View.Summary.class})
    private final List<T> recs;

    public Page(long total, List<T> recs) {
        this.total = total;
        this.recs = recs;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getRecs() {
        return recs;
    }

}
