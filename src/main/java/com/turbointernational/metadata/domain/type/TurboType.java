package com.turbointernational.metadata.domain.type;
import com.turbointernational.metadata.domain.Manufacturer;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord

// TODO: Waiting on Paul to hear on uniqueness:
// https://github.com/zero-one/TurboInternational/pull/28/files#L0R121
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class TurboType {

    @ManyToOne
    @Column(name="manfr_id", nullable=false)
    private Manufacturer manufacturer;

    @Column(nullable=false)
    private String name;

    private Long importPk;
}
