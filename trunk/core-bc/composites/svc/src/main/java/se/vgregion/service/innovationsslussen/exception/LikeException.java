package se.vgregion.service.innovationsslussen.exception;

/**
 * The Class LikeException.
 */
public class LikeException extends Exception {

    private static final long serialVersionUID = 1067821640902803246L;

    /**
     * Instantiates a new like exception.
     */
    public LikeException() {
        super();
    }

    /**
     * Instantiates a new like exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public LikeException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new like exception.
     *
     * @param arg0 the arg0
     */
    public LikeException(String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new like exception.
     *
     * @param arg0 the arg0
     */
    public LikeException(Throwable arg0) {
        super(arg0);
    }



}
