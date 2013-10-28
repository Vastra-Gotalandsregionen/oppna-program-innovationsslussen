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
