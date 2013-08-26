package se.vgregion.service.barium;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;

/**
 * @author Claes Lundahl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:service-context-it.xml"})
public class BariumServiceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(BariumServiceIT.class);

    BariumService bariumService;

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:service-context-it.xml");
        bariumService = (BariumService) ctx.getBean("bariumService");
    }

    @Test
    public void getBariumIdea() throws Exception {
        IdeaObjectFields result = bariumService.getBariumIdea("4563a84a-b19c-4c75-90d8-1ef18c2bebcc");
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getState());
    }


    @Test
    public void getObject() throws Exception {
        ObjectEntry result = bariumService.getObject("4563a84a-b19c-4c75-90d8-1ef18c2bebcc");
        Assert.assertNotNull(result);
        System.out.println(result.getState());
    }


}
