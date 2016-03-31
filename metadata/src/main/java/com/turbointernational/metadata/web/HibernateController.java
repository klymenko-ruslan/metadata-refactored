package com.turbointernational.metadata.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/hibernate")
public class HibernateController {

    private static final Logger log = LoggerFactory.getLogger(HibernateController.class);

    @PersistenceContext
    private EntityManager em;

    @RequestMapping("/clear")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    @Transactional
    public void clear() throws Exception {
        em.clear();
        log.info("Hibernate cache has been reset.");
    }

}
