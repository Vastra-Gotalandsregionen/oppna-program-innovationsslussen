package se.vgregion.service.barium;

/**
 * The Class BariumException.
 */
public class BariumException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new barium exception.
     *
     * @param message the message
     */
    public BariumException(String message) {
        super(message);
    }

    /**
     * Instantiates a new barium exception.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public BariumException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Instantiates a new barium exception.
     *
     * @param e the exception
     */
    public BariumException(Throwable e) {
        super(e);
    }
}