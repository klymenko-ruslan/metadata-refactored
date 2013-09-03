package com.turbointernational.metadata.domain.part;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@Configurable
@Entity
@RooJpaActiveRecord
@RooJson
@SecondaryTable(name="NOZZLE_RING", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class NozzleRing extends Part {
}
