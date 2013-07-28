package com.turbointernational.metadata.domain.bom;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bomalternativeheaders")
@Controller
@RooWebScaffold(path = "bomalternativeheaders", formBackingObject = BOMAlternativeHeader.class)
public class BOMAlternativeHeaderController {
}
