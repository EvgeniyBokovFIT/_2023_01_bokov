import message.Message;
import message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialization.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler implements Runnable {
    private static final List<ClientHandler> handlers = new CopyOnWriteArrayList<>();
    private static final String UNEXPECTED_MESSAGE_TYPE = "Unexpected message type ";
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private final Socket socket;
    private String username;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private final Serializer serializer = new Serializer();

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            Message receivedMessage = serializer.readMessage(this.inputStream);
            if (receivedMessage.messageType() != MessageType.CONNECTION) {
                log.error(UNEXPECTED_MESSAGE_TYPE + receivedMessage.messageType());
                throw new IllegalArgumentException(UNEXPECTED_MESSAGE_TYPE + receivedMessage.messageType());
            }

            this.username = receivedMessage.username();
            if (usernameExists(this.username)) {
                Message errorMessage = new Message(MessageType.ERROR, "Username already exists", this.username);
                sendToThisClient(errorMessage);
                return;
            }
            sendToThisClient(new Message(MessageType.SUCCESS, "Connected successfully", this.username));
            handlers.add(this);

            sendToAll(new Message(MessageType.NEW_MEMBER, getUserListAsString(), this.username));

            while (!Thread.currentThread().isInterrupted()) {
                Message message = serializer.readMessage(this.inputStream);
                sendToAll(new Message(message.messageType(), message.message(), this.username));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            finish();
        }
    }

    private void finish() {
        if (handlers.contains(this)) {
            removeThisHandler();
        }
        sendUserListToClients();
        try {
            this.socket.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        Thread.currentThread().interrupt();
    }

    private void removeThisHandler() {
        Message message = new Message(MessageType.DISCONNECT, " has disconnected from the chat", this.username);
        sendToAll(message);
        handlers.remove(this);
    }

    private void sendUserListToClients() {
        String users = getUserListAsString();
        sendToAll(new Message(MessageType.USER_LIST, users, ""));
    }

    private static String getUserListAsString() {
        StringBuilder users = new StringBuilder();
        for (ClientHandler handler : handlers) {
            users.append(handler.username).append("\n");
        }
        return users.toString();
    }


    private void sendToAll(Message message) {
        for (ClientHandler clientHandler : handlers) {
            clientHandler.sendToThisClient(message);
        }
    }

    private void sendToThisClient(Message message) {
        try {
            serializer.writeMessage(this.outputStream, message);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            disconnect();
            Thread.currentThread().interrupt();
        }
    }

    public void disconnect() {
        handlers.remove(this);
    }

    private boolean usernameExists(String username) {
        for (ClientHandler handler : handlers) {
            if (handler.username.equals(username)) {
                return true;
            }
        }
        return false;
    }
}
