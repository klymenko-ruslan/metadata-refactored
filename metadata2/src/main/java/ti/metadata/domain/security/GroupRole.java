package ti.metadata.domain.security;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(identifierType = GroupRolePK.class, table = "group_role")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "groupId", "roleId" })
public class GroupRole {
}
