package se.vgregion.service.innovationsslussen.exception;

/**
 * The Class UpdateIdeaException.
 */
public class UpdateIdeaException extends Exception {

    private static final long serialVersionUID = -3433601651327182392L;

    /**
     * Instantiates a new update idea exception.
     */
    public UpdateIdeaException() {
        super();
    }

    /**
     * Instantiates a new update idea exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public UpdateIdeaException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new update idea exception.
     *
     * @param arg0 the arg0
     */
    public UpdateIdeaException(String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new update idea exception.
     *
     * @param arg0 the arg0
     */
    public UpdateIdeaException(Throwable arg0) {
        super(arg0);
    }



}
