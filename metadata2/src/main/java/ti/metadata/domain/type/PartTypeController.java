package ti.metadata.domain.type;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/parttypes")
@Controller
@RooWebScaffold(path = "parttypes", formBackingObject = PartType.class)
public class PartTypeController {
}
