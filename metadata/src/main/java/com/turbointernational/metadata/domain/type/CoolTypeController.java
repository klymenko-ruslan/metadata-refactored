package com.turbointernational.metadata.domain.type;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = CoolType.class)
@Controller
@RequestMapping("/cooltypes")
public class CoolTypeController {
}
