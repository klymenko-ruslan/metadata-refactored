package ti.metadata.domain.bom;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(table = "bom_alt_item")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "bomAltHeaderId", "bomId", "partId" })
public class BomAltItem {
}
