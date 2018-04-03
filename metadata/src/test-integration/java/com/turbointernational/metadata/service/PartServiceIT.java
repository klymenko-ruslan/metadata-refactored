package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.Application.TEST_SKIPFILEIO;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.turbointernational.metadata.AbstractFunctionalIT;
import com.turbointernational.metadata.entity.part.ProductImage;

public class PartServiceIT extends AbstractFunctionalIT {

    @Autowired
    private PartService partService;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/part_service/add_product_image.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("mock@gmail.com")
    public void testAddProductImage() throws Exception {
        System.setProperty(TEST_SKIPFILEIO, "true");
        partService.addProductImage(25861L, TRUE, null);
        ProductImage pi = em.find(ProductImage.class, 1L);
        assertNotNull(pi);
    }

}
