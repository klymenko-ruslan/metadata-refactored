package ti.metadata.domain.part;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(table = "cartridge")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "part" })
public class Cartridge {
}
