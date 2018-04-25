package com.turbointernational.metadata.web.controller;

import static com.turbointernational.metadata.entity.User.SYNC_AGENT_USER_ID;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.UserDao;
import com.turbointernational.metadata.entity.Mas90Sync;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.service.Mas90SyncService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Page;

/**
 * Created by dmytro.trunykov@zorallabs.com on 13.01.16.
 */
@Controller
@RequestMapping(value = { "/mas90sync", "/metadata/mas90sync" })
public class Mas90SyncController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private Mas90SyncService mas90SyncService;

    @RequestMapping(value = "/status", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_MAS90_SYNC")
    public Mas90SyncService.SyncProcessStatus status() {
        Mas90SyncService.SyncProcessStatus retVal = mas90SyncService.status();
        return retVal;
    }

    @RequestMapping(value = "/history", method = GET)
    @ResponseBody
    @Transactional
    @JsonView(View.Summary.class)
    @Secured("ROLE_MAS90_SYNC")
    public Page<Mas90Sync> history(@RequestParam(name = "start") int startPosition,
            @RequestParam(name = "max") int maxResults) {
        return mas90SyncService.history(startPosition, maxResults);
    }

    @RequestMapping(value = "/start", method = POST)
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

    /**
     * Start synchronization process.
     *
     * This method can be called by CURL or WGET from localhost to start the
     * process. So expression below <code>hasIpAddress('127.0.0.1/32')</code> is
     * mandatory. Don't remove it.
     *
     * @return
     */
    @RequestMapping(value = "/startsyncjob", method = POST)
    @ResponseBody
    @Transactional
    @JsonView(View.Summary.class)
    @Secured("ROLE_MAS90_SYNC")
    public Mas90SyncService.SyncProcessStatus start(HttpServletResponse response) throws IOException {
        User syncAgent = userDao.findOne(SYNC_AGENT_USER_ID);
        if (syncAgent == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "User [" + SYNC_AGENT_USER_ID + "] not found.");
            return null;
        }
        if (!syncAgent.isEnabled()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "User account [" + SYNC_AGENT_USER_ID + "] is disabled.");
            return null;
        }
        Mas90SyncService.SyncProcessStatus status = mas90SyncService.start(syncAgent);
        return status;
    }

    @RequestMapping(value = "/result/{id}", method = GET)
    @ResponseBody
    @Transactional
    @JsonView(View.Detail.class)
    @Secured("ROLE_MAS90_SYNC")
    public Mas90Sync result(@PathVariable("id") Long id) throws IOException {
        Mas90Sync retVal = mas90SyncService.result(id);
        return retVal;
    }

}
