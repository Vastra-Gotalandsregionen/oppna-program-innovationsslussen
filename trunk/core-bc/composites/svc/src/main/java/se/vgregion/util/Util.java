package se.vgregion.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Patrik Bergström
 */
public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

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
