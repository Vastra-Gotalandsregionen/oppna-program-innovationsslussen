package se.vgregion.service.spring;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: simongoransson
 * Date: 2013-11-19
 * Time: 15:39
 * To change this template use File | Settings | File Templates.
 */
public class ContextUtil {

    @Autowired
    private ApplicationContext ctx;

    private static ApplicationContext staticContext;

    @PostConstruct
    public void init() {
        staticContext = ctx;
    }

    public static ApplicationContext getApplicationContext() {
        return staticContext;
    }


}
