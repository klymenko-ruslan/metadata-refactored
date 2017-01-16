package com.turbointernational.metadata.web.controller;

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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/16/17.
 */
@Controller
@RequestMapping(value = {"/changelog/source", "/metadata/changelog/source"})
public class ChangelogSourceController {

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @RequestMapping(value = "/sourcename/list", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public List<SourceName> getAllChangelogSourceNames() {
        return changelogSourceService.getAllChangelogSourceNames();
    }

    @RequestMapping(method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public Source findChangelogSourceByName(@RequestParam("name") String name) {
        return changelogSourceService.findChangelogSourceByName(name);
    }

    @RequestMapping(method = POST)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public Source createChanlelogSource(@RequestBody Source source) {
        return changelogSourceService.createChangelogSource(source);
    }

}
