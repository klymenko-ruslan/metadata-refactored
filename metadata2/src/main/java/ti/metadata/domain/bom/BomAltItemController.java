package ti.metadata.domain.bom;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bomaltitems")
@Controller
@RooWebScaffold(path = "bomaltitems", formBackingObject = BomAltItem.class)
public class BomAltItemController {
}
