package exception;

public class FileException extends RuntimeException{
    private static final String FILENAME_MESSAGE = ". Название файла:";

    public FileException(String message, String filename) {
        super(message + FILENAME_MESSAGE + filename);
    }

    public FileException(String message, String filename, Exception cause) {
        super(message + FILENAME_MESSAGE + filename, cause);
    }

}
