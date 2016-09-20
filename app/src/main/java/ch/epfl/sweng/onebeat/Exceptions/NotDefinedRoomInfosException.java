package ch.epfl.sweng.onebeat.Exceptions;

/**
 * Created by M4ttou on 03.12.2015.
 */
public class NotDefinedRoomInfosException extends Throwable {
    private static final long serialVersionUID = 1L;

    public NotDefinedRoomInfosException() {
        super();
    }
    public NotDefinedRoomInfosException(String message) {
        super(message);
    }
    public NotDefinedRoomInfosException(Throwable throwable) {
        super(throwable);
    }
}
