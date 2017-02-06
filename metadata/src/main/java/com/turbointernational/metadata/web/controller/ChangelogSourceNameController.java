package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import com.turbointernational.metadata.service.ChangelogSourceNameService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-01-27.
 */
@Controller
@RequestMapping(value = {"/changelog/source/name", "/metadata/changelog/source/name"})
public class ChangelogSourceNameController {

    @Autowired
    private ChangelogSourceNameService changelogSourceNameService;

    @JsonInclude(ALWAYS)
    public static class SourceNameRequest {

        @JsonView(View.Summary.class)
        private String name;

        public SourceNameRequest() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    @RequestMapping(method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_CHLOGSRCNAME_READ")
    public SourceName findChangelogSourceNameByName(@RequestParam("name") String name) {
        return changelogSourceNameService.findChangelogSourceNameByName(name);
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_CHLOGSRCNAME_DELETE")
    public boolean remove(@PathVariable Long id) {
        return changelogSourceNameService.remove(id);
    }

    @RequestMapping(value = "/list", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_CHLOGSRCNAME_READ")
    public List<SourceName> getAllChangelogSourceNames() {
        List<SourceName> retVal = changelogSourceNameService.getAllChangelogSourceNames();
        return retVal;
    }

    @RequestMapping(value = "/filter", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_CHLOGSRCNAME_READ")
    public Page<SourceName> filterChangelogSourceNames(
            @RequestParam(name = "sortProperty", required = false) String sortProperty,
            @RequestParam(name = "sortOrder", required = false) String sortOrder,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit) {
        return changelogSourceNameService.filterChangelogSourceNames(sortProperty, sortOrder, offset, limit);
    }

    @RequestMapping(method = POST)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_CHLOGSRCNAME_CREATE")
    public SourceName create(@RequestBody SourceNameRequest snr) throws IOException {
        return changelogSourceNameService.create(snr.getName());
    }

    @RequestMapping(path = "/{id}", method = PUT)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_CHLOGSRCNAME_UPDATE")
    public SourceName update(@PathVariable("id") Long id, @RequestBody SourceNameRequest snr) throws IOException {
        return changelogSourceNameService.update(id, snr.getName());
    }

}
