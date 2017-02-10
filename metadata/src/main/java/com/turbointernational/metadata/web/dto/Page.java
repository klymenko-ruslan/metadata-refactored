package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

/**
 * Created by dmytro.trunykov on 21.03.16.
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

}
