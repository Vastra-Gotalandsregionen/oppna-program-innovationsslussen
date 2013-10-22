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

import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Patrik Bergström
 */
public class BariumUpdateConsumerTest {

    // Tests the parsing of the xml.
    @Test
    public void testOnMessage() throws Exception {

        // Given
        String messageText = "<objects>\n" +
                "  <object objectId=\"c1ecb049-6249-4063-98d9-b6aec068b368\" objectClassNamespace=\"barium.bbe.dataforms.dataform\" instanceId=\"aaaaaa\" dataId=\"IDE\" newObject=\"false\" updateDate=\"2013-09-24T09:34:00.167\" />\n" +
                "  <object objectId=\"c1ecb049-6249-4063-98d9-b6aec068b368\" objectClassNamespace=\"barium.bbe.dataforms.dataform\" instanceId=\"bbbbbb\" dataId=\"IDE\" newObject=\"false\" updateDate=\"2013-09-24T09:34:00.167\" />\n" +
                "</objects>";

        TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText(messageText);

        IdeaService mock = mock(IdeaService.class);

        BariumUpdateConsumer consumer = new BariumUpdateConsumer(mock);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        // When
        consumer.onMessage(textMessage);

        // Then
        verify(mock).updateIdeasFromBarium(captor.capture());
        assertEquals(Arrays.asList("aaaaaa", "bbbbbb"), captor.getValue());
    }
}
