import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerService {
    private static final Logger log = LoggerFactory.getLogger(ServerService.class);

    public static void startServer() {
        try {
            Server server = new Server(PropertiesParser.parsePort());
            server.run();
        } catch (ServerException e) {
            log.error(e.getMessage(), e);
        }
    }
}
