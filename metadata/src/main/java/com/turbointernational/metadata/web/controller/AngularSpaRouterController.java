package com.turbointernational.metadata.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;;

/**
 * @author jrodriguez
 */
@Controller
public class AngularSpaRouterController {

    @RequestMapping(method = GET, produces = {TEXT_HTML_VALUE, "!" +APPLICATION_JSON_VALUE},
        value = {
        "/",
        "/part/**",
        "/service/list",
        "/changelog/list",
        "/parttype/**",
        "/other/**",
        "/security/**",
        "/password/**",
        "/application/**",
        "/bom/**",
        "/changelog/**",
        "/changelog/source/**",
        "/search/indexing/status",
        "/mas90/sync/status",
        "/manufacturer/list",
        "/changelog/source/name/list",
//        "/",
//        "/part/\\d+",
//        "/part/\\d+/component/search",
//        "/part/\\d+/bom/search",
//        "/part/\\d+/bom/\\d+/search",
//        "/part/\\d+/ancestors",
//        "/part/list",
//        "/part/createByPartTypeId/\\d+",
//        "/part/Kit/\\d+/component/search",
//        "/other/turboModels",
//        "/security/me",
//        "/security/password/reset/.*",
//        "/security/groups",
//        "/security/groups/\\d+",
//        "/security/users",
//        "/security/users/\\d+",
//        "/password/reset/.*"
    }
)
    public String index() {
        return "forward:/index.html";
    }
}
