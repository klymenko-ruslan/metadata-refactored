
package com.turbointernational.metadata.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.BOMService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Status;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/status/")
public class StatusController {

    @Autowired
    private BOMService bomService;

    // @Transactional
    @RequestMapping(value = "/all")
    @JsonView(View.Summary.class)
    @ResponseBody
    @Secured("ROLE_READ")
    public Status status() throws Exception {
        Status status = new Status();
        // status.setBomRebuilding(false);
        BOMService.IndexingStatus rebuildStatus = bomService.getRebuildStatus();
        status.setBomRebuilding(rebuildStatus.isRebuilding());
        return status;
    }
}
