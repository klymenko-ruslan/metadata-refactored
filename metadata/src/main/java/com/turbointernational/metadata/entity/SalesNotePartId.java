package com.turbointernational.metadata.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.turbointernational.metadata.entity.part.Part;

/**
 *
 * @author jrodriguez
 */
@Embeddable
public class SalesNotePartId implements Serializable {

    private static final long serialVersionUID = 1281174981465341464L;

    @ManyToOne
    private SalesNote salesNote;

    @ManyToOne(fetch = FetchType.LAZY)
    private Part part;

    public SalesNotePartId() {
    }

    public SalesNotePartId(SalesNote salesNote, Part part) {
        this.salesNote = salesNote;
        this.part = part;
    }

    public SalesNote getSalesNote() {
        return salesNote;
    }

    public void setSalesNote(SalesNote salesNote) {
        this.salesNote = salesNote;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SalesNotePartId that = (SalesNotePartId) o;

        if (getSalesNote() != null ? !salesNote.equals(that.salesNote) : that.getSalesNote() != null) {
            return false;
        }
        if (getPart() != null ? !part.equals(that.part) : that.getPart() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.getSalesNote() != null ? this.getSalesNote().hashCode() : 0);
        hash = 97 * hash + (this.getPart() != null ? this.getPart().hashCode() : 0);
        return hash;
    }

}
