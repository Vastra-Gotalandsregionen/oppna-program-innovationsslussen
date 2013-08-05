package se.vgregion.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Patrik Bergström
 */
public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    public static String escapeHtmlWithLineBreaks(String text) {
        if (text == null) {
            return null;
        }
        return StringEscapeUtils.escapeHtml(text)
                .replaceAll("\\r\\n", "\n")
                .replaceAll("\\n\\r", "\n")
                .replaceAll("\\n", "<br/>")
                .replaceAll("\\r", "<br/");
    }

    public static void closeClosables(Closeable... closables) {
        for (Closeable closeable : closables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

}
