package se.vgregion.service.jms.factory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import se.vgregion.service.jms.BariumUpdateConsumer;

/**
 * @author Patrik Bergström
 */
public class JmsContainerFactory implements FactoryBean<DefaultMessageListenerContainer> {

    private static DefaultMessageListenerContainer theInstance;
    private String destinationName;
    private BariumUpdateConsumer messageListener;
    private ActiveMQConnectionFactory connectionFactory;

    @Override
    public DefaultMessageListenerContainer getObject() throws Exception {
        if (theInstance != null) {
            return theInstance;
        }

        if (connectionFactory != null) {
            DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
            container.setDestinationName(destinationName);
            container.setMessageListener(messageListener);
            container.setConnectionFactory(connectionFactory);
            theInstance = container;
            return theInstance;
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultMessageListenerContainer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setMessageListener(BariumUpdateConsumer messageListener) {
        this.messageListener = messageListener;
    }

    public BariumUpdateConsumer getMessageListener() {
        return messageListener;
    }

    public void setConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ActiveMQConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }
}
