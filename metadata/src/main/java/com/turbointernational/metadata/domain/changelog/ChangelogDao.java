package com.turbointernational.metadata.domain.changelog;

import com.turbointernational.metadata.domain.GenericDao;
import com.turbointernational.metadata.domain.security.User;
import java.util.Date;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ChangelogDao extends GenericDao<Changelog> {
    
    public ChangelogDao() {
        super(Changelog.class);
    }
    
    @Transactional
    public Changelog log(String description, String data) {
        Changelog changelog = new Changelog();
        changelog.setDescription(description);
        changelog.setChangeDate(new Date());
        changelog.setData(data);
        changelog.setUser(User.getCurrentUser());
        
        persist(changelog);
        
        return changelog;
    }
    
}
