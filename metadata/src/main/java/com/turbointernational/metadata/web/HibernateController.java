package com.turbointernational.metadata.web;

import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/hibernate")
public class HibernateController {
    private static final Logger log = Logger.getLogger(HibernateController.class.toString());
    
    @PersistenceContext
    EntityManager em;
    
    
    @RequestMapping("/clear")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void clear() throws Exception {
        em.clear();
    }
    
}
