package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.GasketType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="GASKET", inheritanceType = "JOINED")
public class Gasket extends Part {

    @ManyToOne
    @JoinColumn(name="gasket_type_id")
    private GasketType type;
}
