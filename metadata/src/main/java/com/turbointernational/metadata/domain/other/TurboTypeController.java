package com.turbointernational.metadata.domain.other;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/other/turbo")
@Controller
@RooWebJson(jsonObject = TurboType.class)
public class TurboTypeController {
}
