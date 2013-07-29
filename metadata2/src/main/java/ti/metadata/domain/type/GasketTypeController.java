package ti.metadata.domain.type;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/gaskettypes")
@Controller
@RooWebScaffold(path = "gaskettypes", formBackingObject = GasketType.class)
public class GasketTypeController {
}
