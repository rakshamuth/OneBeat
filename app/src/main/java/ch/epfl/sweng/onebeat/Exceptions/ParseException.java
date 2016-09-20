package ch.epfl.sweng.onebeat.Exceptions;

/**
 * Created by hugo on 25.10.2015.
 */
public class ParseException extends Throwable {

    private static final long serialVersionUID = 1L;

    public ParseException() {
        super();
    }
    public ParseException(String message) {
        super(message);
    }
    public ParseException(Throwable throwable) {
        super(throwable);
    }
}
