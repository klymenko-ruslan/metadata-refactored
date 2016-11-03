package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.web.dto.Page;
import com.turbointernational.metadata.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by dmytro.trunykov@zorallabs.com on 09.08.16.
 */
@Controller
@RequestMapping(value = {"/changelog", "/metadata/changelog"})
public class ChangelogController {

    @Autowired
    private ChangelogService changelogService;

    @RequestMapping(value = "/list", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_ADMIN")
    public Page<Changelog> filterChangelog(@RequestParam(name = "userId", required = false) Long userId,
                                           @RequestParam(name = "startDate", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar startDate,
                                           @RequestParam(name = "finishDate", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar finishDate,
                                           @RequestParam(name = "description", required = false) String description,
                                           @RequestParam(name = "data", required = false) String data,
                                           @RequestParam(name = "sortProperty", required = false) String sortProperty,
                                           @RequestParam(name = "sortOrder", required = false) String sortOrder,
                                           @RequestParam(name = "offset", required = false) Integer offset,
                                           @RequestParam(name = "limit", required = false) Integer limit) {
        return changelogService.filter(userId, startDate, finishDate, description, data,
                sortProperty, sortOrder, offset, limit);
    }

}
