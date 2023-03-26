package exception;

public class ShapeException extends RuntimeException{
    public ShapeException(String message) {
        super(message);
    }

    public ShapeException(String message, Exception cause) {
        super(message, cause);
    }
}
