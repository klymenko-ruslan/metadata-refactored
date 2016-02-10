package com.turbointernational.metadata.domain.changelog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.domain.security.User;
import java.io.Serializable;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ChangelogDao extends AbstractDao<Changelog> {
    
    @Autowired(required=true)
    ObjectMapper json;
    
    public ChangelogDao() {
        super(Changelog.class);
    }

    @Transactional
    public Changelog log(String description) {
        return log(description, "");
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
    
    @Transactional
    public Changelog log(String description, Serializable data) {
        try {
            Changelog changelog = new Changelog();
            changelog.setDescription(description);
            changelog.setChangeDate(new Date());
            changelog.setData(json.writeValueAsString(data));
            changelog.setUser(User.getCurrentUser());

            persist(changelog);

            return changelog;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize data.", e);
        }
    }
    
}
