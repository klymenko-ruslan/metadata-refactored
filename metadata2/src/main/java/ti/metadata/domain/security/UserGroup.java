package ti.metadata.domain.security;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(identifierType = UserGroupPK.class, table = "user_group")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "userId", "groupId" })
public class UserGroup {
}
