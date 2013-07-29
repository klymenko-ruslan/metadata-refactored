package ti.metadata.domain.interchange;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/interchangeheaders")
@Controller
@RooWebScaffold(path = "interchangeheaders", formBackingObject = InterchangeHeader.class)
public class InterchangeHeaderController {
}
