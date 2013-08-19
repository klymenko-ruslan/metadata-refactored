package com.turbointernational.metadata.domain.bom;
import com.turbointernational.metadata.domain.bom.BOMItem;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = BOMItem.class)
@Controller
@RequestMapping("/bomitems")
public class BOMItemController {
}
