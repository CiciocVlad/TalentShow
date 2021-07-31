package talent.server;

public class ServicesException extends Exception {
    public ServicesException(String msg) {
        super(msg);
    }

    public ServicesException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ServicesException(Throwable cause) {
        super(cause);
    }
}
