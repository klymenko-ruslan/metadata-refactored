package com.turbointernational.metadata.domain.part;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="CARTRIDGE", inheritanceType = "JOINED")
@DiscriminatorValue(value = "2")
@PrimaryKeyJoinColumn(name = "part_id")
public class Cartridge extends Part {

}
