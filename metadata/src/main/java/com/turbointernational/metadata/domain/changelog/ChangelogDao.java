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
        User user = User.getCurrentUser();
        return log(user, description, "");
    }

    @Transactional
    public Changelog log(User user, String description) {
        return log(user, description, "");
    }

    @Transactional
    public Changelog log(String description, String data) {
        User user = User.getCurrentUser();
        return log(user, description, data);
    }

    @Transactional
    public Changelog log(User user, String description, String data) {
        Changelog changelog = new Changelog();
        changelog.setDescription(description);
        changelog.setChangeDate(new Date());
        changelog.setData(data);
        changelog.setUser(user);
        persist(changelog);
        return changelog;
    }

    @Transactional
    public Changelog log(String description, Serializable data) {
        User user = User.getCurrentUser();
        return log(user, description, data);
    }

    @Transactional
    public Changelog log(User user, String description, Serializable data) {
        try {
            Changelog changelog = new Changelog();
            changelog.setDescription(description);
            changelog.setChangeDate(new Date());
            changelog.setData(json.writeValueAsString(data));
            changelog.setUser(user);

            persist(changelog);

            return changelog;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize data.", e);
        }
    }
    
}
