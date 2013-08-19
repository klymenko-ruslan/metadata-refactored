package com.turbointernational.metadata.domain.type;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = SealType.class)
@Controller
@RequestMapping("/sealtypes")
public class SealTypeController {
}
