package ti.metadata.domain.part;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(identifierType = StandardJournalBearingPK.class, table = "standard_journal_bearing")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "standardPartId", "oversizedPartId" })
public class StandardJournalBearing {
}
