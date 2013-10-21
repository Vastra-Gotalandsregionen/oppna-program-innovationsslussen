package se.vgregion.service.jms.factory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author Patrik Bergström
 */
public class JmsTemplateFactory implements FactoryBean<JmsTemplate> {

    private static JmsTemplate theInstance;
    private ActiveMQConnectionFactory connectionFactory;

    @Override
    public JmsTemplate getObject() throws Exception {
        if (theInstance != null) {
            return theInstance;
        }

        if (connectionFactory != null) {
            JmsTemplate template = new JmsTemplate(connectionFactory);
            theInstance = template;
            return theInstance;
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getObjectType() {
        return JmsTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ActiveMQConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }
}
