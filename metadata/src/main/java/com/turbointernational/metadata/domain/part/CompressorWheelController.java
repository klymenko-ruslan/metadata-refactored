package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/part/compressorwheels")
@Controller
@RooWebScaffold(path = "part/compressorwheels", formBackingObject = CompressorWheel.class)
@RooWebJson(jsonObject = CompressorWheel.class)
public class CompressorWheelController {
}
