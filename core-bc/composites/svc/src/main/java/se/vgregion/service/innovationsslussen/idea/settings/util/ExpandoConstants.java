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

package se.vgregion.service.innovationsslussen.idea.settings.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class holds constants for the settings service.
 * 
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public final class ExpandoConstants {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpandoConstants.class.getName());

    private ExpandoConstants() {
        throw new UnsupportedOperationException();
    }
    
    // Add This (Social Sharing)
    public static final String ADD_THIS_CODE = "addThisCode";
    

    // Barium
    public static final String BARIUM_DETAILS_VIEW_URL_PREFIX = "innovationsslussenBariumDetailsViewUrlPrefix";
    
    // Pages
    public static final String FRIENDLY_URL_FAQ = "faqFriendlyURL";
    public static final String FRIENDLY_URL_CREATE_IDEA = "createIdeaFriendlyURL";
    
    // Piwik
    public static final String PIWIK_CODE = "piwikCode";

    // Email notifications
    public static final String NOTIFICATION_EMAIL_ACTIVE = "notificationEmailActive";
    public static final String NOTIFICATION_EMAIL_FROM = "notificationEmailForm";
    public static final String NOTIFICATION_EMAIL_SUBJECT = "notificationEmailSubject";
    public static final String NOTIFICATION_EMAIL_PUBLIC_BODY = "notificationEmailPublicBody";
    public static final String NOTIFICATION_EMAIL_PRIVATE_BODY = "notificationEmailPrivateBody";

    // Server
    public static final String SERVER_NAME_URL = "serverNameUrl";


    

}