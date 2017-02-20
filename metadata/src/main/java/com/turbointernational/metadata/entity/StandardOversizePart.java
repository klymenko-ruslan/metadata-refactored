package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-13.
 */
@Entity
@Table(name = "standard_oversize_part")
@NamedQueries({
        @NamedQuery(
                name = "findOversizeParts",
                query = "select distinct s.pk.oversize from StandardOversizePart s " +
                        "where s.pk.standard.id=:partId order by s.pk.oversize.id"
        ),
        @NamedQuery(
                name = "findStandardParts",
                query = "select distinct s.pk.standard from StandardOversizePart s " +
                        "where s.pk.oversize.id=:partId order by s.pk.standard.id"
        )
})
public class StandardOversizePart implements Serializable {

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
