package com.turbointernational.metadata.domain.security;
import com.turbointernational.metadata.domain.security.User;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
@Controller
@RooWebScaffold(path = "users", formBackingObject = User.class)
public class UserController {
}
