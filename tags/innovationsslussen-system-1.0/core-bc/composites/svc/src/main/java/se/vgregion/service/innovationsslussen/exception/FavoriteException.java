package se.vgregion.service.innovationsslussen.exception;

/**
 * The Class FavoriteException.
 */
public class FavoriteException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7173396209424362762L;

    /**
     * Instantiates a new favorite exception.
     */
    public FavoriteException() {
        super();
    }

    /**
     * Instantiates a new favorite exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public FavoriteException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new favorite exception.
     *
     * @param arg0 the arg0
     */
    public FavoriteException(String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new favorite exception.
     *
     * @param arg0 the arg0
     */
    public FavoriteException(Throwable arg0) {
        super(arg0);
    }



}
