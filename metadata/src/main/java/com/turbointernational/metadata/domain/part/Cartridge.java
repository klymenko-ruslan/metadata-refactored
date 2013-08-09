package com.turbointernational.metadata.domain.part;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord
@SecondaryTable(name="CARTRIDGE", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Cartridge extends Part {

}
