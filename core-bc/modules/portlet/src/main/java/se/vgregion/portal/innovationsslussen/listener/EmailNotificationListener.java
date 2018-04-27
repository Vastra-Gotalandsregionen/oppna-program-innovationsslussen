package se.vgregion.portal.innovationsslussen.listener;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;

import javax.mail.internet.InternetAddress;
import java.util.ArrayList;

/**
 * 
 * @author Simon Göransson
 * @company Monator Technologies AB
 */

public class EmailNotificationListener implements MessageListener {

    public void receive(Message message) {

        try {
            _log.debug("EmailNotificationListener recived a message");

            JSONObject jsonObject = (JSONObject) message.getPayload();
            JSONArray emailToArray = jsonObject.getJSONArray("emailTo");
            String emailFrom = jsonObject.getString("emailFrom");
            String subject = jsonObject.getString("subject");
            String body = jsonObject.getString("body");

            // Sending Email.
            ArrayList<InternetAddress> arrayList = new ArrayList<InternetAddress>();

            for (int i = 0; i < emailToArray.length(); i++) {
               String emailTo = emailToArray.getString(i);
               arrayList.add(new InternetAddress(emailTo));
            }

            InternetAddress[] to = arrayList.toArray(new InternetAddress[]{} );
            InternetAddress from = new InternetAddress(emailFrom);
            InternetAddress[] noOne = new InternetAddress[] {};

            MailMessage mailMessage = new MailMessage();

            mailMessage.setFrom(from);
            mailMessage.setTo(to);
            mailMessage.setReplyTo(noOne);
            mailMessage.setSubject(subject);
            mailMessage.setBody(body);
            mailMessage.setHTMLFormat(true);

            MailServiceUtil.sendEmail(mailMessage);
            _log.debug("EmailNotificationListener - Email sent!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Log _log = LogFactoryUtil.getLog(EmailNotificationListener.class);
}
