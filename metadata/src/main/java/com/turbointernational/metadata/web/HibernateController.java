package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.part.Part;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
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
    
    @RequestMapping("/clear")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void clear() throws Exception {
        EntityManager em = Part.entityManager();

        em.clear();
        
//        Session s = (Session) em.getDelegate();
//        SessionFactory sf = s.getSessionFactory();
//        sf.getCache().evictCollectionRegions();
//        sf.getCache().evictEntityRegions();
//        sf.getCache().evictNaturalIdRegions();
//        sf.getCache().evictQueryRegions();
    }
    
}
