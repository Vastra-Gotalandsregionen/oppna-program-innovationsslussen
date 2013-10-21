package se.vgregion.service.innovationsslussen.exception;

/**
 * The Class PermissionCheckerException.
 */
public class PermissionCheckerException extends Exception {

    private static final long serialVersionUID = -3788118533211706040L;

    /**
     * Instantiates a new permission checker exception.
     */
    public PermissionCheckerException() {
        super();
    }

    /**
     * Instantiates a new permission checker exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public PermissionCheckerException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new permission checker exception.
     *
     * @param arg0 the arg0
     */
    public PermissionCheckerException(String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new permission checker exception.
     *
     * @param arg0 the arg0
     */
    public PermissionCheckerException(Throwable arg0) {
        super(arg0);
    }



}
