package com.turbointernational.metadata.domain.type;
import com.turbointernational.metadata.domain.other.Manufacturer;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Table(name="TURBO_TYPE", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class TurboType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="manfr_id", nullable=false)
    private Manufacturer manufacturer;

    @Column(nullable=false)
    private String name;
}
