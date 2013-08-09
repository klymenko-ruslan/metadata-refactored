package com.turbointernational.metadata.domain.interchange;
import com.turbointernational.metadata.domain.part.Part;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord(table="INTERCHANGE_HEADER")
public class Interchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="INTERCHANGE_ITEM",
               joinColumns=@JoinColumn(name="interchange_header_id"),
               inverseJoinColumns=@JoinColumn(name="part_id"))
    private Set<Part> parts;
}
