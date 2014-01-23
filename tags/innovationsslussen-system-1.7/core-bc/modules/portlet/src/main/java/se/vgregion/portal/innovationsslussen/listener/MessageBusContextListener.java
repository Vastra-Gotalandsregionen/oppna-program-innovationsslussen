package se.vgregion.portal.innovationsslussen.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.SynchronousDestination;

public final class MessageBusContextListener implements ServletContextListener {

    private static String DESTINATION = "vgr/email/notification";
    private static String LISTENER = "EmailNotificationListener";
    private static Log _log = LogFactoryUtil.getLog(MessageBusContextListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        // Starting the email notification listener.
        ServletContext servletContext = servletContextEvent.getServletContext();

        try {

            SynchronousDestination destination = new SynchronousDestination();
            destination.setName(DESTINATION);
            MessageBusUtil.addDestination(destination);

            EmailNotificationListener emailNotificationListener = new EmailNotificationListener();
            MessageBusUtil.registerMessageListener(DESTINATION, emailNotificationListener);
            servletContext.setAttribute(LISTENER, emailNotificationListener);
            _log.info("Application EmailNotificationListener is starting");

        } catch (Exception e) {
            _log.error("Error setting context attribute: " + e.getMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        // Shutting down the email notification listener.
        ServletContext servletContext = servletContextEvent.getServletContext();
        MessageBusUtil.unregisterMessageListener(DESTINATION, (MessageListener) servletContext.getAttribute(LISTENER));

        MessageBusUtil.removeDestination(DESTINATION);
        _log.info("Application EmailNotificationListener is shutting down");
    }
}