package ti.metadata.domain.interchange;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(table = "interchange_item")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "part", "interchangeHeaderId" })
public class InterchangeItem {
}
