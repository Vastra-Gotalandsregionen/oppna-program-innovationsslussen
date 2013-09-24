package se.vgregion.portal.innovationsslussen;
/**
 * 
 */

/**
 * The BaseController.
 * 
 * @author Simon Göransson - simon.goransson@monator.com - vgrid: simgo3
 */
public class BaseController {

    protected Throwable getLastCause(Exception exception) {
        Throwable cause = exception;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }
}
