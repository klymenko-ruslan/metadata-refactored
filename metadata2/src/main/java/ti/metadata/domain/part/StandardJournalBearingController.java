package ti.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/standardjournalbearings")
@Controller
@RooWebScaffold(path = "standardjournalbearings", formBackingObject = StandardJournalBearing.class)
public class StandardJournalBearingController {
}
