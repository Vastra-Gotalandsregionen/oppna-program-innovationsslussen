package se.vgregion.service.innovationsslussen.ldap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-08-14
 * Time: 09:52
 * Used to give information about what field (name) in a ldap database the one in a class represents. Used in beans
 * that are passed to the LdapService.find method.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExplicitLdapName {

    /**
     * Used to give information about what field (name) in a ldap database the one in a class represents. Used in beans
     * that are passed to the LdapService.find method.
     *
     * @return the string
     */
    String value();

}
