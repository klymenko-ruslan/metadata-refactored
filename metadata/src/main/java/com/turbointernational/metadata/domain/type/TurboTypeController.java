package com.turbointernational.metadata.domain.type;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/type/turbo")
@Controller
@RooWebJson(jsonObject = TurboType.class)
public class TurboTypeController {
}
