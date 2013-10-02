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
    

}