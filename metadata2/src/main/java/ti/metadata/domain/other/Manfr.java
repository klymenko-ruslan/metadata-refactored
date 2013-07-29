package ti.metadata.domain.other;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(table = "manfr")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "manfrs", "parts", "turboTypes", "manfrTypeId", "parentManfrId" })
public class Manfr {
}
