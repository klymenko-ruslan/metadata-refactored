package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Configurable
@Entity
@RooJpaActiveRecord
@SecondaryTable(name="cartridge", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Cartridge extends Part {

}
