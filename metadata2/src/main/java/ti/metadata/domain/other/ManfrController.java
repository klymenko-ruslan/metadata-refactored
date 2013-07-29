package ti.metadata.domain.other;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/manfrs")
@Controller
@RooWebScaffold(path = "manfrs", formBackingObject = Manfr.class)
public class ManfrController {
}
