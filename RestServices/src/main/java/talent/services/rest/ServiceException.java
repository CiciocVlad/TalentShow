package talent.services.rest;

public class ServiceException extends RuntimeException {
    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Exception e) {
        super(e);
    }
}
