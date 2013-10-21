package se.vgregion.service.innovationsslussen.exception;

/**
 * The Class CreateIdeaException.
 */
public class CreateIdeaException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4929280397863823051L;

    /**
     * Instantiates a new creates the idea exception.
     */
    public CreateIdeaException() {
        super();
    }

    /**
     * Instantiates a new creates the idea exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public CreateIdeaException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new creates the idea exception.
     *
     * @param arg0 the arg0
     */
    public CreateIdeaException(String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new creates the idea exception.
     *
     * @param arg0 the arg0
     */
    public CreateIdeaException(Throwable arg0) {
        super(arg0);
    }



}
