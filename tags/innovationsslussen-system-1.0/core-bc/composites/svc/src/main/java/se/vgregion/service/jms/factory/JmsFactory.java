package se.vgregion.service.jms.factory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author Patrik Bergström
 */
public class JmsFactory implements FactoryBean<ActiveMQConnectionFactory> {

    private static ActiveMQConnectionFactory theInstance;
    private String brokerUrl;

    @Override
    public ActiveMQConnectionFactory getObject() throws Exception {
        if (theInstance != null) {
            return theInstance;
        }

        if (brokerUrl != null && !brokerUrl.contains("${") && !"".equals(brokerUrl)) {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            theInstance = factory;
            return factory;
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getObjectType() {
        return ActiveMQConnectionFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }
}
