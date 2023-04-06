package exception;

public class FileException extends RuntimeException{
    private static final String FILENAME_MESSAGE = ". Название файла:";

    public FileException(String message, String filename) {
        this(message, filename, null);
    }

    public FileException(String message, String filename, Exception cause) {
        super(message + FILENAME_MESSAGE + filename, cause);
    }
}
