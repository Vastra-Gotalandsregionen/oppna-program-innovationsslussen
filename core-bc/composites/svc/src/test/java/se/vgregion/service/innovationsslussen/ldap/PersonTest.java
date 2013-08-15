package se.vgregion.service.innovationsslussen.ldap;

import junit.framework.Assert;
import org.apache.commons.collections.BeanMap;
import org.junit.Test;

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
        Person person = new Person();
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
        Person person = new Person();
        Assert.assertEquals(Person.Gender.UNKNOWN, person.getGender());
        person.setHsaPersonIdentityNumber("abcdefghijklomnopqrstuvxyzåäö");
        Assert.assertEquals(Person.Gender.UNKNOWN, person.getGender());
        person.setHsaPersonIdentityNumber("abcdefghijkl");
        Assert.assertEquals(Person.Gender.UNKNOWN, person.getGender());

        person.setHsaPersonIdentityNumber("abcdefghij1l");
        Assert.assertEquals(Person.Gender.MALE, person.getGender());

        person.setHsaPersonIdentityNumber("abcdefghij2l");
        Assert.assertEquals(Person.Gender.FEMALE, person.getGender());
    }

}
