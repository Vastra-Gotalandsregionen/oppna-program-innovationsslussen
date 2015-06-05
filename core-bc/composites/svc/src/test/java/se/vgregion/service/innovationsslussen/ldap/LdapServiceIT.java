/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

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
        AdPerson person = new AdPerson();
        person.setHsaIdentity(filter);
        List<AdPerson> result = ldapService.find(person);
        printFirstItem(result);
    }


    @Test
    public void findByVgrId() {
        String filter = "marsc9";
        AdPerson person = new AdPerson();
        //person.setVgrId(filter);
        person.setCn(filter);
        List<AdPerson> result = ldapService.find(person);
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
