package com.turbointernational.metadata.domain.part;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@SecondaryTable(name="CARTRIDGE", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Cartridge extends Part {

}
