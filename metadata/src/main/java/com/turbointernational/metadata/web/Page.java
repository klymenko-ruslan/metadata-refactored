package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

/**
 * Created by dmytro.trunykov on 21.03.16.
 */
public class Page<T> {

    @JsonView({View.Summary.class})
    private final long total;

    @JsonView({View.Summary.class})
    private final List<T> recs;

    public Page(long total, List<T> recs) {
        this.total = total;
        this.recs = recs;
    }

}
