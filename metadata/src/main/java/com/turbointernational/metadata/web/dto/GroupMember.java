package com.turbointernational.metadata.web.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(ALWAYS)
public class GroupMember implements Serializable {

    private static final long serialVersionUID = 8825483248046659973L;

    /**
     * Group ID.
     */
    @JsonView(View.Summary.class)
    private Long id;

    /**
     * Group name.
     */
    @JsonView(View.Summary.class)
    private String name;

    /**
     * Roles of the group.
     */
    @JsonView(View.Summary.class)
    private List<String> roles;

    /**
     * Is user member of the group or not.
     */
    @JsonView(View.Summary.class)
    private Boolean isMember;

    public GroupMember() {
    }

    public GroupMember(Long id, String name, List<String> roles, Boolean isMember) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.isMember = isMember;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Boolean getIsMember() {
        return isMember;
    }

    public void setIsMember(Boolean isMember) {
        this.isMember = isMember;
    }

}
