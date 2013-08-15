package se.vgregion.service.innovationsslussen.ldap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-08-13
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:ldap-config.xml"})
public class LdapServiceIT {

    LdapService ldapService;

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:ldap-config.xml");
        ldapService = (LdapService) ctx.getBean("ldapService");
    }

    @Test
    public void findByHsaId() {
        String filter = "SE2321000131-P000000233140";
        Person person = new Person();
        person.setHsaIdentity(filter);
        List<Person> result = ldapService.find(person);
        System.out.println("result " + result);
    }


    @Test
    public void findByVgrId() {
        String filter = "clalu4";
        Person person = new Person();
        person.setVgrId(filter);
        List<Person> result = ldapService.find(person);
        System.out.println("result " + result);
    }

}
