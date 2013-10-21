package se.vgregion.service.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * @author Patrik Bergström
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:messaging-context.xml", "classpath:mock-context.xml",
        "classpath:test-jpa-infrastructure-configuration.xml", "classpath:broker-url-property.xml"})
public class JmsFactoryWithPropertyTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void testApplicationContext() {
        ActiveMQConnectionFactory bean = ctx.getBean(ActiveMQConnectionFactory.class);

        assertNotNull(bean);
    }
}
