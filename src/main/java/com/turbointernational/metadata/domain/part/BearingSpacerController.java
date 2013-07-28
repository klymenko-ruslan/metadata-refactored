package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.BearingSpacer;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bearingspacers")
@Controller
@RooWebScaffold(path = "bearingspacers", formBackingObject = BearingSpacer.class)
public class BearingSpacerController {
}
