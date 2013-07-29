package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/pistonrings")
@Controller
@RooWebScaffold(path = "pistonrings", formBackingObject = PistonRing.class)
public class PistonRingController {
}
