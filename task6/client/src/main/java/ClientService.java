import controller.ClientToServerController;
import controller.ConnectionInitializer;
import controller.ServerToClientController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ChatWindow;
import view.ConnectionWindow;

import java.io.IOException;

public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    public static void startClient() {
        ConnectionInitializer connectionInitializer = new ConnectionInitializer();
        ChatWindow chatWindow = new ChatWindow();
        ConnectionWindow connectionWindow = new ConnectionWindow(chatWindow);
        connectionWindow.setConnectionButtonListener(connectionInitializer);

        connectionInitializer.setErrorListener(connectionWindow);
        connectionInitializer.addSuccessListener(connectionWindow);
        connectionInitializer.addSuccessListener(chatWindow);
        connectionWindow.showWindow();

        try {
            ClientToServerController clientToServerController = new ClientToServerController(connectionInitializer.getSocket());
            chatWindow.setMessageListener(clientToServerController);

            ServerToClientController serverToClientController = new ServerToClientController(connectionInitializer.getSocket());
            serverToClientController.setServerMessageListener(chatWindow);
            serverToClientController.setUserListListener(chatWindow);
            serverToClientController.handleServerMessages();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            connectionInitializer.closeSocket();
        }
    }
}
