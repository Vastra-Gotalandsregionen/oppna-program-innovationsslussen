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

package se.vgregion.service.barium;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;

/**
 * @author Claes Lundahl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:service-context-it.xml"})
public class BariumServiceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(BariumServiceIT.class);

    BariumService bariumService;

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:service-context-it.xml");
        bariumService = (BariumService) ctx.getBean("bariumService");
    }

    @Test
    public void getBariumIdea() throws Exception {
        IdeaObjectFields result = bariumService.getBariumIdea("4563a84a-b19c-4c75-90d8-1ef18c2bebcc");
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getIde());
    }


    @Test
    public void getObject() throws Exception {
        ObjectEntry result = bariumService.getObject("4563a84a-b19c-4c75-90d8-1ef18c2bebcc");
        Assert.assertNotNull(result);
        System.out.println(result.getState());
    }


}
