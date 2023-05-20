import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final String SERVER_ERROR_MESSAGE = "Ошибка сервера";
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private final int port;

    public Server(int port) throws ServerException {
        this.port = port;
    }

    public void run() throws ServerException {
        Socket socket = null;
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (true) {
                socket = serverSocket.accept();
                try {
                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread messageHandlerThread = new Thread(clientHandler);
                    messageHandlerThread.start();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    socket.close();
                }
            }
        } catch (IOException e) {
            log.error(SERVER_ERROR_MESSAGE, e);
            throw new ServerException(SERVER_ERROR_MESSAGE, e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

    }
}
