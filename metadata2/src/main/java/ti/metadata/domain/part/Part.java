package ti.metadata.domain.part;
import javax.persistence.Column;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(table = "part")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "backplate", "bearingHousing", "bearingSpacer", "cartridge", "compressorWheel", "gasket", "heatshield", "interchangeItem", "journalBearing", "kit", "nozzleRing", "pistonRing", "turbineWheel", "turbo", "boms", "boms1", "bomAltItems", "manfrId", "partTypeId" })
public class Part {

    @Column(name = "inactive", columnDefinition = "BIT", length = 1)
    private Boolean inactive;
}
