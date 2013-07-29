package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.Turbo;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/turboes")
@Controller
@RooWebScaffold(path = "turboes", formBackingObject = Turbo.class)
public class TurboController {
}
