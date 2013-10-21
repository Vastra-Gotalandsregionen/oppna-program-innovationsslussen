package se.vgregion.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

/**
 * An Util class for escapeing html in an String.
 * 
 * @author Patrik Bergström
 */
public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    public static SQLException getNextExceptionFromLastCause(Exception e) {
        Throwable lastCause = getLastCause(e);
        SQLException nextException = null;
        if (lastCause instanceof SQLException) {
            nextException = ((SQLException) lastCause).getNextException();
        }
        return nextException;
    }

    public static Throwable getLastCause(Exception exception) {
        Throwable cause = exception;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }

    /**
     * Escape html with line breaks.
     *
     * @param text the text
     * @return the string
     */
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

    /**
     * Close closables.
     *
     * @param closables the closables
     */
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
