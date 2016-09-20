package ch.epfl.sweng.onebeat.Exceptions;

/**
 * Created by Matthieu on 16.11.2015.
 */
public class JSONParserException extends Exception {
    private static final long serialVersionUID = 1L;

    public JSONParserException() {
        super();
    }
    public JSONParserException(String message) {
        super(message);
    }
    public JSONParserException(Throwable throwable) {
        super(throwable);
    }
}
