package com.turbointernational.metadata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Value;
import com.jayway.restassured.RestAssured;


/**
 *
 * @author jrodriguez
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ResourceTest {
    
    @Value("${local.server.port}")
    int port;
    
    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testListSalesNotes() {
        System.out.println("testListSalesNotes");
        System.out.println(RestAssured.when().get("/salesNotes").prettyPrint());
    }
}
