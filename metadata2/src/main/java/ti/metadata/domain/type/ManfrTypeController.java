package ti.metadata.domain.type;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/manfrtypes")
@Controller
@RooWebScaffold(path = "manfrtypes", formBackingObject = ManfrType.class)
public class ManfrTypeController {
}
