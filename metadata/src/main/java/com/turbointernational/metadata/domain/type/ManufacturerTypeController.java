package com.turbointernational.metadata.domain.type;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/manufacturertypes")
@Controller
@RooWebScaffold(path = "manufacturertypes", formBackingObject = ManufacturerType.class)
public class ManufacturerTypeController {
}
