package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(name="nozzle_ring")
@PrimaryKeyJoinColumn(name = "part_id")
public class NozzleRing extends Part {
}
