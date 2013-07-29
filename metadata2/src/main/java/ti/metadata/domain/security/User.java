package ti.metadata.domain.security;
import javax.persistence.Column;
import org.springframework.roo.addon.dbre.RooDbManaged;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(table = "user")
@RooDbManaged(automaticallyDelete = true)
@RooToString(excludeFields = { "userGroups" })
public class User {

    //@Column(name = "enabled", columnDefinition = "BIT", length = 1)
    @Column(name = "enabled", columnDefinition = "BIT")
    private Boolean enabled;
}
