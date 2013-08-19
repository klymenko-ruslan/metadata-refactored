package com.turbointernational.metadata.domain.type;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = GasketType.class)
@Controller
@RequestMapping("/gaskettypes")
public class GasketTypeController {
}
