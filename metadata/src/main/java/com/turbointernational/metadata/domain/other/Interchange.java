package com.turbointernational.metadata.domain.other;
import com.turbointernational.metadata.domain.part.Part;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooJson
@RooJpaActiveRecord(table = "interchange_header")
public class Interchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "interchange", fetch = FetchType.LAZY)
    private Set<Part> parts;
}
