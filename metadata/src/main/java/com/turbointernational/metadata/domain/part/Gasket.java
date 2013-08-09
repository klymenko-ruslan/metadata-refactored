package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.GasketType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord
@SecondaryTable(name="gasket", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Gasket extends Part {

    @OneToOne
    @JoinColumn(name="gasket_type_id", table = "gasket")
    private GasketType type;
}
