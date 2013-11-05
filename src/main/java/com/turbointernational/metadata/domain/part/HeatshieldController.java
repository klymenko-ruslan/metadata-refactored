package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.Heatshield;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/heatshields")
@Controller
@RooWebScaffold(path = "heatshields", formBackingObject = Heatshield.class)
public class HeatshieldController {
}
