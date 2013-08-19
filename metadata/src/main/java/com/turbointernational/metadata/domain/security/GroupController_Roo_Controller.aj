// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.security;

import com.turbointernational.metadata.domain.security.Group;
import com.turbointernational.metadata.domain.security.GroupController;
import com.turbointernational.metadata.domain.security.Role;
import com.turbointernational.metadata.domain.security.User;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect GroupController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String GroupController.create(@Valid Group group, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, group);
            return "security/groups/create";
        }
        uiModel.asMap().clear();
        group.persist();
        return "redirect:/security/groups/" + encodeUrlPathSegment(group.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String GroupController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Group());
        return "security/groups/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String GroupController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("group", Group.findGroup(id));
        uiModel.addAttribute("itemId", id);
        return "security/groups/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String GroupController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("groups", Group.findGroupEntries(firstResult, sizeNo));
            float nrOfPages = (float) Group.countGroups() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("groups", Group.findAllGroups());
        }
        return "security/groups/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String GroupController.update(@Valid Group group, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, group);
            return "security/groups/update";
        }
        uiModel.asMap().clear();
        group.merge();
        return "redirect:/security/groups/" + encodeUrlPathSegment(group.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String GroupController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Group.findGroup(id));
        return "security/groups/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String GroupController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Group group = Group.findGroup(id);
        group.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/security/groups";
    }
    
    void GroupController.populateEditForm(Model uiModel, Group group) {
        uiModel.addAttribute("group", group);
        uiModel.addAttribute("roles", Role.findAllRoles());
        uiModel.addAttribute("users", User.findAllUsers());
    }
    
    String GroupController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
