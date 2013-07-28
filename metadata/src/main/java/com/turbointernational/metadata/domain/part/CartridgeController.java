package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.Cartridge;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/cartridges")
@Controller
@RooWebScaffold(path = "cartridges", formBackingObject = Cartridge.class)
public class CartridgeController {
}
