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

    // Barium
    public static final String BARIUM_DETAILS_VIEW_URL_PREFIX = "innovationsslussenBariumDetailsViewUrlPrefix";

}