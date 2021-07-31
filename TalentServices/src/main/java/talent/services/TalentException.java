package talent.services;

public class TalentException extends Exception {
    public TalentException(String msg) {
        super(msg);
    }

    public TalentException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TalentException(Throwable cause) {
        super(cause);
    }
}
