package com.turbointernational.metadata.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Entity
@Table(name = "user_group")
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 572164403927340467L;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGroup() == null) ? 0 : getGroup().hashCode());
        result = prime * result + ((getUser() == null) ? 0 : getUser().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserGroup other = (UserGroup) obj;
        if (getGroup() == null) {
            if (other.getGroup() != null)
                return false;
        } else if (!getGroup().equals(other.getGroup()))
            return false;
        if (getUser() == null) {
            if (other.getUser() != null)
                return false;
        } else if (!getUser().equals(other.getUser()))
            return false;
        return true;
    }

}
