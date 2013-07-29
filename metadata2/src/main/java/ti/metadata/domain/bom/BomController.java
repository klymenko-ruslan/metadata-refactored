package ti.metadata.domain.bom;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/boms")
@Controller
@RooWebScaffold(path = "boms", formBackingObject = Bom.class)
public class BomController {
}
