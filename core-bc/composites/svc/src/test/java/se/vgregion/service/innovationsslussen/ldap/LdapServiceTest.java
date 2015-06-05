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

import junit.framework.Assert;
import org.apache.commons.collections.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-08-13
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */

public class LdapServiceTest {

    LdapService service;

    @Before
    public void setUp() {
        service = new LdapService();
    }

    @Test
    public void find() {
        service.setLdapTemplate(Mockito.mock(LdapTemplate.class));
        List<Individual> result = service.find(new Individual());
    }

    @Test
    public void newAttributesMapper() throws NamingException {
        AttributesMapper mapper = service.newAttributesMapper(Individual.class);
        Assert.assertNotNull(mapper);
        BasicAttributes attributes = new BasicAttributes();
        attributes.put("middleName", "middleNameValue");
        attributes.put("nameNotInBean", "nameNotInBean");
        Individual result = (Individual) mapper.mapFromAttributes(attributes);
        Assert.assertEquals("middleNameValue", result.getMiddleName());
    }

    @Test
    public void toBeanPropertyName() {
        String result = service.toBeanPropertyName("alta;vista");
        Assert.assertEquals("altaVista", result);

        result = service.toBeanPropertyName("alta-vista");
        Assert.assertEquals("altaVista", result);
    }

    @Test
    public void removeSignFrom() {
        String result = service.removeSignFrom("beanPropertyName", "signNotInPropertyName");
        Assert.assertEquals("beanPropertyName", result);

        result = service.removeSignFrom("bean-Property-Name", "-");
        Assert.assertEquals("beanPropertyName", result);
    }

    @Test
    public void newAttributeFilter() {
        Filter result = service.newAttributeFilter("name", "value");
        Assert.assertEquals("(name=value)", result.encode());

        result = service.newAttributeFilter("name", "value*");
        Assert.assertEquals("(name=value*)", result.encode());
    }

    @Test
    public void toAndCondition() {
        Individual individual = new Individual();
        individual.setVgrId("*");
        individual.setHsaIdentity("foo");
        individual.setExtraName("en");
        AndFilter result = service.toAndCondition(individual);
        Assert.assertNotNull(result);
    }

    @Test
    public void getPlainNameOrExplicit() {
        String result = service.getPlainNameOrExplicit(Individual.class, "extraName");
        Assert.assertEquals("foo", result);

        result = service.getPlainNameOrExplicit(Individual.class, "hsaIdentity");
        Assert.assertEquals("hsaIdentity", result);

        result = service.getPlainNameOrExplicit(Individual.class, "middleName");
        Assert.assertEquals("middleName", result);
    }

    public static class Individual extends KivPerson {
        private String middleName;

        @ExplicitLdapName("foo")
        private String extraName;

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getExtraName() {
            return extraName;
        }

        public void setExtraName(String extraName) {
            this.extraName = extraName;
        }
    }

}
