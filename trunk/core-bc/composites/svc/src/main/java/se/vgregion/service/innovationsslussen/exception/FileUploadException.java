package se.vgregion.service.innovationsslussen.exception;

/**
 * The Class FileUploadException.
 */
public class FileUploadException extends Exception {

    private static final long serialVersionUID = -5516537708695347390L;

    /**
     * Instantiates a new file upload exception.
     */
    public FileUploadException() {
        super();
    }

    /**
     * Instantiates a new file upload exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public FileUploadException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new file upload exception.
     *
     * @param arg0 the arg0
     */
    public FileUploadException(String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new file upload exception.
     *
     * @param arg0 the arg0
     */
    public FileUploadException(Throwable arg0) {
        super(arg0);
    }



}
