package com.turbointernational.metadata.domain.bom;
import com.turbointernational.metadata.domain.bom.BOMAlternativeHeader;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = BOMAlternativeHeader.class)
@Controller
@RequestMapping("/bomalternativeheaders")
public class BOMAlternativeHeaderController {
}
