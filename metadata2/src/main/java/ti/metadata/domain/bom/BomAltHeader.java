package ti.metadata.domain.bom;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(table = "bom_alt_header")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "bomAltItems" })
public class BomAltHeader {
}
