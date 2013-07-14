package com.turbointernational.metadata.domain;
import com.turbointernational.metadata.domain.part.Part;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="INTERCHANGE_HEADER")
public class Interchange {

    @Column(nullable=false)
    private String name;

    private String description;

    @ManyToMany
    @JoinTable(name="INTERCHANGE_ITEM",
               joinColumns=@JoinColumn(name="interchange_header_id"),
               inverseJoinColumns=@JoinColumn(name="part_id"))
    private List<Part> parts;
}
