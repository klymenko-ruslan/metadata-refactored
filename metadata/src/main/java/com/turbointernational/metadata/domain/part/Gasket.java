package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.GasketType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@SecondaryTable(name="GASKET", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Gasket extends Part {

    @ManyToOne
    @JoinColumn(name="gasket_type_id", table = "GASKET")
    private GasketType type;
}
