package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/part/bearinghousings")
@Controller
@RooWebScaffold(path = "part/bearinghousings", formBackingObject = BearingHousing.class)
public class BearingHousingController {
}
