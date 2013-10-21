package se.vgregion.service.innovationsslussen.ldap;

import org.apache.commons.collections.BeanMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.HashMap;
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
@SuppressWarnings("unchecked")
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
        printFirstItem(result);
    }


    @Test
    public void findByVgrId() {
        String filter = "marsc9";
        Person person = new Person();
        //person.setVgrId(filter);
        person.setCn(filter);
        List<Person> result = ldapService.find(person);
        printFirstItem(result);
    }

    private void printFirstItem(Collection collection) {
        if (collection.isEmpty()) {
            System.out.println("\nNo result!\n");
            return;
        }
        HashMap map = new HashMap(new BeanMap(collection.iterator().next()));
        System.out.println("\n\nSample item: \n");
        for (Object key: map.keySet()) {
            System.out.println(key + " = " + map.get(key));
        }
    }

}
