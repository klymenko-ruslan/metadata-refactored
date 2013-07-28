package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.TurbineWheel;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/turbinewheels")
@Controller
@RooWebScaffold(path = "turbinewheels", formBackingObject = TurbineWheel.class)
public class TurbineWheelController {
}
