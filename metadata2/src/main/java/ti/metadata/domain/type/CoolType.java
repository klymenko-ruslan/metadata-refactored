package ti.metadata.domain.type;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(table = "cool_type")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "turboes" })
public class CoolType {
}
