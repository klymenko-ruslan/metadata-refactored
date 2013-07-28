package com.turbointernational.metadata.domain.type;
import com.turbointernational.metadata.domain.type.CoolType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/cooltypes")
@Controller
@RooWebScaffold(path = "cooltypes", formBackingObject = CoolType.class)
public class CoolTypeController {
}
