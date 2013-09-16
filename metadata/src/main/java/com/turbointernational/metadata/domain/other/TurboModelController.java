package com.turbointernational.metadata.domain.other;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@Controller
@RequestMapping("/other/turbo-model")
@RooWebJson(jsonObject = TurboModel.class)
public class TurboModelController {
}
