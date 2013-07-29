package ti.metadata.domain.interchange;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/interchangeitems")
@Controller
@RooWebScaffold(path = "interchangeitems", formBackingObject = InterchangeItem.class)
public class InterchangeItemController {
}
