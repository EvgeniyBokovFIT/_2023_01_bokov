package controller;

import message.Message;
import message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialization.Serializer;
import view.ConnectionButtonListener;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionInitializer implements ConnectionButtonListener {
    private static final String UNEXPECTED_MESSAGE_TYPE = "Unexpected message type";
    private static final String INVALID_IP_OR_PORT = "Invalid ip or port";
    private static final Logger log = LoggerFactory.getLogger(ConnectionInitializer.class);

    private Socket socket;
    private ConnectionErrorListener errorListener;
    private final List<ConnectionSuccessListener> successListeners = new ArrayList<>();
    private final Serializer serializer = new Serializer();

    @Override
    public void onConnectionParametersEntered(String username, String ip, String port) {
        try {
            int portInt = Integer.parseInt(port);
            this.socket = new Socket(ip, portInt);
            Message message = new Message(MessageType.CONNECTION, "", username);
            serializer.writeMessage(this.socket.getOutputStream(), message);
            Message receivedMessage = this.serializer.readMessage(socket.getInputStream());
            if (receivedMessage.messageType() == MessageType.SUCCESS) {
                this.successListeners.forEach(ConnectionSuccessListener::onSuccessfulConnection);
                return;
            }
            if (receivedMessage.messageType() == MessageType.ERROR) {
                this.errorListener.onConnectionError(receivedMessage);
                return;
            }
            log.error(UNEXPECTED_MESSAGE_TYPE);
            throw new IllegalArgumentException(UNEXPECTED_MESSAGE_TYPE);

        } catch (IOException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            log.error(INVALID_IP_OR_PORT);

            if (this.errorListener != null) {
                Message message = new Message(MessageType.ERROR, INVALID_IP_OR_PORT, "");
                this.errorListener.onConnectionError(message);
            }
            throw new IllegalArgumentException(INVALID_IP_OR_PORT);
        }
    }

    public void setErrorListener(ConnectionErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void addSuccessListener(ConnectionSuccessListener successListeners) {
        this.successListeners.add(successListeners);
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void closeSocket() {
        try {
            this.socket.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
