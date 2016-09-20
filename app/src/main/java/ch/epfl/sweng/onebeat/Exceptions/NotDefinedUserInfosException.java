package ch.epfl.sweng.onebeat.Exceptions;

/**
 * Created by Matthieu on 26.11.2015.
 */
public class NotDefinedUserInfosException extends Exception {

    private static final long serialVersionUID = 1L;

    public NotDefinedUserInfosException() {
        super();
    }
    public NotDefinedUserInfosException(String message) {
        super(message);
    }
    public NotDefinedUserInfosException(Throwable throwable) {
        super(throwable);
    }
}
