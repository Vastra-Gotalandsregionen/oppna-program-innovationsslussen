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

package se.vgregion.service.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNull;

/**
 * @author Patrik Bergström
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:messaging-context.xml", "classpath:mock-context.xml", "classpath:test-jpa-infrastructure-configuration.xml"})
public class JmsFactoryNoPropertyTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void testApplicationContext() {
        ActiveMQConnectionFactory bean = ctx.getBean(ActiveMQConnectionFactory.class);

        assertNull(bean);
    }
}
