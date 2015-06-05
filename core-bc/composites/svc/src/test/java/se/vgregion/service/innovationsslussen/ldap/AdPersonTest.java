package se.vgregion.service.innovationsslussen.ldap;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestResult;
import org.junit.Test;

/**
 * Created by clalu4 on 2015-06-04.
 */
public class AdPersonTest {

    @Test
    public void toFormattedVgrStrukturPersonText() {
        AdPerson person = new AdPerson();
        String input = "ou=Arkitektur & Utveckling,ou=Applikation & Utveckling,ou=VGR IT,ou=Koncernkontoret,ou=Regionstyrelsen,ou=Org,o=VGR";
        String goal = "VGR/Org/Regionstyrelsen/Koncernkontoret/VGR IT/Applikation & Utveckling/Arkitektur & Utveckling";
        String result = person.toFormattedVgrStrukturPersonText(input);
        System.out.println(result);
        Assert.assertEquals(goal, result);
    }
}