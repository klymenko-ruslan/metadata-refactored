package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.NozzleRing;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/nozzlerings")
@Controller
@RooWebScaffold(path = "nozzlerings", formBackingObject = NozzleRing.class)
public class NozzleRingController {
}
