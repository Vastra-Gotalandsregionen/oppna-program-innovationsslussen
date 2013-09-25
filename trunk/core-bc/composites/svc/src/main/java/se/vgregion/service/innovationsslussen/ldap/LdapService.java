package se.vgregion.service.innovationsslussen.ldap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.apache.commons.collections.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.LikeFilter;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-08-13
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
public class LdapService {

    private LdapTemplate ldapTemplate;

    public LdapTemplate getLdapTemplate() {
        return ldapTemplate;
    }

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    /**
     * Finds data from the ldap server. Provide a structure (class instance) with the data to use as search criteria
     * and gets the answer as a list with the same format (class type) as the criteria.
     * @param sample holds properties that (could) match fields in the db by the operator '=' or 'like' (in conjunction
     *               with having a '*' character in a String value).
     *
     * @param <T> type of the param and type of the answers inside the resulting list.
     * @return a list of search hits.
     */
    public <T> List<T> find(T sample) {
        final AttributesMapper mapper = newAttributesMapper(sample.getClass());
        final Filter searchFilter = toAndCondition(sample);
        final SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        @SuppressWarnings("unchecked")
        List<T> result = ldapTemplate.search(StringUtils.EMPTY, searchFilter.encode(), searchControls,
                mapper);

        return result;
    }

    AttributesMapper newAttributesMapper(final Class type) {
        return new AttributesMapper() {
            @Override
            public Object mapFromAttributes(Attributes attributes) throws NamingException {
                try {
                    return mapFromAttributesImpl(attributes);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public Object mapFromAttributesImpl(Attributes attributes) throws NamingException,
            IllegalAccessException, InstantiationException {
                Object result = type.newInstance();
                BeanMap bm = new BeanMap(result);
                NamingEnumeration<? extends Attribute> all = attributes.getAll();

                while (all.hasMore()) {
                    Attribute attribute = all.next();

                    String name = toBeanPropertyName(attribute.getID());
                    if (bm.containsKey(name) && bm.getWriteMethod(name) != null) {
                        bm.put(name, attribute.get());
                    }
                }
                return result;
            }
        };
    }

    String toBeanPropertyName(String name) {
        name = removeSignFrom(name, ";");
        name = removeSignFrom(name, "-");
        return name;
    }

    String removeSignFrom(String beanPropertyName, String sign) {
        if (beanPropertyName.contains(sign)) {
            String[] parts = beanPropertyName.split(Pattern.quote(sign));
            StringBuilder sb = new StringBuilder(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                char head = Character.toUpperCase(parts[i].charAt(0));
                String tail = parts[i].substring(1);
                sb.append(head);
                sb.append(tail);
            }
            return sb.toString();
        }
        return beanPropertyName;
    }


    Filter newAttributeFilter(final String name, final String value) {
        Filter filter;

        if (value.contains("*")) {
            filter = new LikeFilter(name, value);
        } else {
            filter = new EqualsFilter(name, value);
        }

        return filter;
    }


    AndFilter toAndCondition(Object obj) {
        AndFilter filter = new AndFilter();
        BeanMap bm = new BeanMap(obj);
        Class type = obj.getClass();
        for (Object entryObj : bm.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) entryObj;
            String property = entry.getKey();
            if (bm.getWriteMethod(property) != null) {
                Object value = entry.getValue();
                if (value != null && !"".equals(value.toString().trim())) {
                    String ldapPropertyName = getPlainNameOrExplicit(type, property);
                    filter.and(newAttributeFilter(ldapPropertyName, value.toString()));
                }
            }
        }
        return filter;
    }

    static String getPlainNameOrExplicit(Class type, String propertyName) {
        try {
            return getPlainNameOrExplicitImpl(type, propertyName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static String getPlainNameOrExplicitImpl(Class type, String propertyName) throws NoSuchFieldException {
        Field field = getField(type, propertyName);
        Annotation annotation = field.getAnnotation(ExplicitLdapName.class);
        if (annotation == null) {
            return propertyName;
        }
        ExplicitLdapName explicitLdapName = (ExplicitLdapName) annotation;
        return explicitLdapName.value();
    }

    static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

}
