package com.turbointernational.metadata.domain.other;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author jrodriguez
 */

@RooWebJson(jsonObject = Manufacturer.class)
@RequestMapping("/other/manufacturer")
@Controller
public class ManufacturerController {
}
