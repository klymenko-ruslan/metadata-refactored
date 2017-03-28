package com.turbointernational.metadata.dao;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.Group;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.UserGroup;

@Repository
public class UserGroupDao extends AbstractDao<UserGroup> {

    public UserGroupDao() {
        super(UserGroup.class);
    }

    public UserGroup addUserToGroup(Long userId, Long groupId) {
        User user = em.getReference(User.class, userId);
        Group group = em.getReference(Group.class, groupId);
        UserGroup ug = new UserGroup();
        ug.setUser(user);
        ug.setGroup(group);
        em.persist(ug);
        return ug;
    }

    public void removeUserFromGroup(Long userId, Long groupId) {
        User user = em.getReference(User.class, userId);
        Group group = em.getReference(Group.class, groupId);
        UserGroup ug = new UserGroup();
        ug.setUser(user);
        ug.setGroup(group);
        ug = em.merge(ug);
        em.remove(ug);
    }

}
