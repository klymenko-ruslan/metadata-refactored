package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.Kit;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/kits")
@Controller
@RooWebScaffold(path = "kits", formBackingObject = Kit.class)
public class KitController {
}
