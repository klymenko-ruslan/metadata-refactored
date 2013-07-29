package ti.metadata.domain.bom;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bomaltheaders")
@Controller
@RooWebScaffold(path = "bomaltheaders", formBackingObject = BomAltHeader.class)
public class BomAltHeaderController {
}
