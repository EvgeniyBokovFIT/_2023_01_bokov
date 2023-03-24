package exception;

public class FileException extends RuntimeException{
    public FileException(String message, Exception exception) {
        super(message, exception);
    }

    public FileException(String message) {
        super(message);
    }
}
