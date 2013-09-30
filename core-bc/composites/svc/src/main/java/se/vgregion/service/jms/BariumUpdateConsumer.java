package se.vgregion.service.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.vgregion.portal.innovationsslussen.domain.xml.BariumNotification;
import se.vgregion.portal.innovationsslussen.domain.xml.BariumNotificationObject;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * JMS consumer service. Listens for messages containing {@link BariumNotification}s which are used to update ideas from
 * Barium.
 *
 * @author Patrik Bergström
 */
public class BariumUpdateConsumer implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BariumUpdateConsumer.class);

    private IdeaService ideaService;

    @Autowired
    public BariumUpdateConsumer(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(BariumNotification.class);

                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                StringReader reader = new StringReader(textMessage.getText());
                BariumNotification notification = (BariumNotification) unmarshaller.unmarshal(reader);

                List<String> ideas = new ArrayList<String>();

                List<BariumNotificationObject> objects = notification.getObjects();

                for (BariumNotificationObject object : objects) {
                    ideas.add(object.getInstanceId());
                }

                LOGGER.info("Updating ideas: " + ideas.toString());

                ideaService.updateIdeasFromBarium(ideas);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
