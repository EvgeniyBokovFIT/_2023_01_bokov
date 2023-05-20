package controller;

import message.Message;
import message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialization.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.time.LocalTime;

public class ServerToClientController {
    private static final Logger log = LoggerFactory.getLogger(ServerToClientController.class);

    private final InputStream in;
    private ServerMessageListener messageListener;
    private UserListListener userListListener;

    private final Serializer serializer = new Serializer();

    public ServerToClientController(Socket socket) throws IOException {
        this.in = socket.getInputStream();
    }

    public void handleServerMessages() {
        while (true) {
            try {
                Message receivedMessage = serializer.readMessage(this.in);
                if (receivedMessage.messageType() == MessageType.DISCONNECT) {
                    this.messageListener.onServerMessage(receivedMessage.username() + " has disconnected from the chat");
                    continue;
                }
                if (receivedMessage.messageType() == MessageType.NEW_MEMBER) {
                    this.userListListener.onUserListUpdate(receivedMessage.message());
                    this.messageListener.onServerMessage(receivedMessage.username() + " has entered the chat");
                    continue;
                }
                if (receivedMessage.messageType() == MessageType.MESSAGE) {
                    LocalTime now = LocalTime.now();
                    this.messageListener.onServerMessage(now.getHour() + ":" + now.getMinute() + " " +
                            receivedMessage.username() + ": " + receivedMessage.message());
                    continue;
                }
                if (receivedMessage.messageType() == MessageType.USER_LIST) {
                    this.userListListener.onUserListUpdate(receivedMessage.message());
                    continue;
                }
                throw new IllegalArgumentException("Unexpected message type " + receivedMessage.messageType());

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void setServerMessageListener(ServerMessageListener listener) {
        this.messageListener = listener;
    }

    public void setUserListListener(UserListListener userListListener) {
        this.userListListener = userListListener;
    }
}
