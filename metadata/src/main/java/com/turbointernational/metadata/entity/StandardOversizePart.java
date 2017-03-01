package com.turbointernational.metadata.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-13.
 */
@Entity
@Table(name = "standard_oversize_part")
public class StandardOversizePart implements Serializable {

    private static final long serialVersionUID = 7476759365829879659L;

    @EmbeddedId
    @JsonView(View.Summary.class)
    private StandardOversizePartId pk;

    public StandardOversizePart() {

    }

    public StandardOversizePart(StandardOversizePartId pk) {
        this.pk = pk;
    }

    public StandardOversizePartId getPk() {
        return pk;
    }

    public void setPk(StandardOversizePartId pk) {
        this.pk = pk;
    }
}
