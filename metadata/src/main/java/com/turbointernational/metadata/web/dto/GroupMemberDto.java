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
public class GroupMemberDto implements Serializable {

    private static final long serialVersionUID = 8825483248046659973L;

    @JsonView(View.Summary.class)
    private String groupName;

    @JsonView(View.Summary.class)
    private List<String> roles;

    @JsonView(View.Summary.class)
    private Boolean isMember;

    public GroupMemberDto() {

    }

    public GroupMemberDto(String groupName, List<String> roles, Boolean isMember) {
        this.groupName = groupName;
        this.roles = roles;
        this.isMember = isMember;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
