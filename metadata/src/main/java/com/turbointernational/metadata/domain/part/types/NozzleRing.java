package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@SecondaryTable(name="nozzle_ring", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class NozzleRing extends Part {
}
