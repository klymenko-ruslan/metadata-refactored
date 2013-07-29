package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.CompressorWheel;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/compressorwheels")
@Controller
@RooWebScaffold(path = "compressorwheels", formBackingObject = CompressorWheel.class)
public class CompressorWheelController {
}
