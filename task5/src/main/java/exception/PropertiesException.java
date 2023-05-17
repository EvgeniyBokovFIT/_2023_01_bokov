package exception;

public class PropertiesException extends Exception {
    public PropertiesException(String message) {
        super(message);
    }

    public PropertiesException(String message, Exception cause) {
        super(message, cause);
    }
}
