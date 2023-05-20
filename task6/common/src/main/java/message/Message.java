package message;

public record Message(
        MessageType messageType,
        String message,
        String username
) {
}
