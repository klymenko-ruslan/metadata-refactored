package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.services.Mas90SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by dmytro.trunykov@zorallabs.com on 13.01.16.
 */
@Controller
@RequestMapping(value = {"/mas90sync", "/metadata/mas90sync"})
public class Mas90SyncController {

    private static Logger log = LoggerFactory.getLogger(Mas90SyncController.class);

    @Autowired
    private Mas90SyncService mas90SyncService;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public Mas90SyncService.SyncProcessStatus status() {
        Mas90SyncService.SyncProcessStatus retVal = mas90SyncService.status();
        return retVal;
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    @JsonView(View.Summary.class)
    @Secured("ROLE_MAS90_SYNC")
    public Page history(@RequestParam(name = "start") int startPosition, @RequestParam(name = "max") int maxResults) {
        return mas90SyncService.history(startPosition, maxResults);
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @JsonView(View.Summary.class)
    @Secured("ROLE_MAS90_SYNC")
    public Mas90SyncService.SyncProcessStatus start(Authentication principal) {
        User user = null;
        if (principal != null) {
            user = (User) principal.getPrincipal();
        }
        Mas90SyncService.SyncProcessStatus status = mas90SyncService.start(user);
        return status;
    }

}
