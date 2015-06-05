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
import org.junit.Test;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;
import sun.net.www.content.image.png;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-08-14
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public class PersonTest {

    @Test
    public void beanStringPropertiesGetterSetters() {
        KivPerson person = new KivPerson();
        BeanMap bm = new BeanMap(person);
        int i = 0;
        for (Object key: bm.keySet()) {
            String name = (String) key;
            if (bm.getWriteMethod(name) != null) {
                if (bm.getType(name).equals(String.class)) {
                    String setterValue = "" + i++;
                    bm.put(key, setterValue);
                    String getterValue = (String) bm.get(key);
                    Assert.assertEquals(setterValue, getterValue);
                }
            }
        }
    }

    @Test
    public void getGender() {
        KivPerson person = new KivPerson();
        Assert.assertEquals(IdeaPerson.Gender.UNKNOWN, person.getGender());
        person.setHsaPersonIdentityNumber("abcdefghijklomnopqrstuvxyzåäö");
        Assert.assertEquals(IdeaPerson.Gender.UNKNOWN, person.getGender());
        person.setHsaPersonIdentityNumber("abcdefghijkl");
        Assert.assertEquals(IdeaPerson.Gender.UNKNOWN, person.getGender());

        person.setHsaPersonIdentityNumber("abcdefghij1l");
        Assert.assertEquals(IdeaPerson.Gender.MALE, person.getGender());

        person.setHsaPersonIdentityNumber("abcdefghij2l");
        Assert.assertEquals(IdeaPerson.Gender.FEMALE, person.getGender());
    }

    @Test
    public void getBirthYear() {
        KivPerson person = new KivPerson();
        Assert.assertNull(person.getBirthYear());
        person.setHsaPersonIdentityNumber("1956MMDD");
        Short result = person.getBirthYear();
        Assert.assertNotNull(result);
        Assert.assertEquals(new Short((short) 1956), result);
    }

}
