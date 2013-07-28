package com.turbointernational.metadata.domain.type;
import com.turbointernational.metadata.domain.type.GasketType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/gaskettypes")
@Controller
@RooWebScaffold(path = "gaskettypes", formBackingObject = GasketType.class)
public class GasketTypeController {
}
