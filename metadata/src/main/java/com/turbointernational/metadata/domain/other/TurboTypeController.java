package com.turbointernational.metadata.domain.other;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@Controller
@RequestMapping("/other/turbo-type")
@RooWebJson(jsonObject = TurboType.class)
public class TurboTypeController {
}
