public class ServerException extends Exception {
    public ServerException(String message, Exception cause) {
        super(message, cause);
    }

    public ServerException(String message) {
        this(message, null);
    }
}
