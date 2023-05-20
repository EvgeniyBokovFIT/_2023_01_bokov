package controller;

import message.Message;
import message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialization.Serializer;
import view.ViewMessageListener;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientToServerController implements ViewMessageListener {
    private static final Logger log = LoggerFactory.getLogger(ClientToServerController.class);

    private final OutputStream out;
    private final Serializer serializer = new Serializer();

    public ClientToServerController(Socket socket) throws IOException {
        this.out = socket.getOutputStream();
    }

    @Override
    public void onMessage(String message) {
        if (message.equals("")) {
            return;
        }
        try {
            serializer.writeMessage(this.out, new Message(MessageType.MESSAGE, message, ""));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
