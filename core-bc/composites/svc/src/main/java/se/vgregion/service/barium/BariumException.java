package se.vgregion.service.barium;

public class BariumException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BariumException(String message)
	  {
	    super(message);
	  }

	public BariumException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BariumException(Throwable e) {
        super(e);
    }
}