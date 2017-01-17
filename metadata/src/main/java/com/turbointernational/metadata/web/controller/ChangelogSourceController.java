package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import com.turbointernational.metadata.service.ChangelogSourceService;
import com.turbointernational.metadata.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/16/17.
 */
@Controller
@RequestMapping(value = {"/changelog/source", "/metadata/changelog/source"})
public class ChangelogSourceController {

    @JsonInclude(ALWAYS)
    public static class SourceRequest {

        @JsonView(View.Summary.class)
        private String name;

        @JsonView(View.Summary.class)
        private String description;

        @JsonView(View.Summary.class)
        private String url;

        @JsonView(View.Summary.class)
        private Long sourceNameId;

        public SourceRequest() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getSourceNameId() {
            return sourceNameId;
        }

        public void setSourceNameId(Long sourceNameId) {
            this.sourceNameId = sourceNameId;
        }

    }

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @RequestMapping(value = "/sourcename/list", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public List<SourceName> getAllChangelogSourceNames() {
        List<SourceName> retVal = changelogSourceService.getAllChangelogSourceNames();
        return retVal;
    }

    @RequestMapping(method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public Source findChangelogSourceByName(@RequestParam("name") String name) {
        Source retVal = changelogSourceService.findChangelogSourceByName(name);
        return retVal;
    }

    @RequestMapping(method = POST)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public Source createChanlelogSource(@RequestBody SourceRequest sr) {
        Source retVal = changelogSourceService.createChangelogSource(sr.getName(), sr.getDescription(),
                sr.getUrl(), sr.getSourceNameId());
        return retVal;
    }

}
