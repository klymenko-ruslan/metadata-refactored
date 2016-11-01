package com.turbointernational.metadata.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author jrodriguez
 */
@Controller
public class AngularSpaRouterController {

    @RequestMapping({
        "/",
        "/part/**",
        "/other/**",
        "/security/**",
        "/password/**",
        "/application/**",
        "/bom/**",
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
    })
    public String index() {
        return "forward:/index.html";
    }
}
