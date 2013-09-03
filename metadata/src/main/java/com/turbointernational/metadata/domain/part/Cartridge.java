package com.turbointernational.metadata.domain.part;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Configurable
@Entity
@RooJpaActiveRecord
@SecondaryTable(name="CARTRIDGE", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Cartridge extends Part {

}
