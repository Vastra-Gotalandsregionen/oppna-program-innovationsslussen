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
