import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Service {
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    public static void startProduction() {
        try {
            int threadId = 1;
            Parameters parameters = PropertiesParser.parse();
            Storage storage = new Storage(parameters.storageSize());

            for (int i = 0; i < parameters.producerCount(); i++) {
                Thread producer = new Thread(new Producer(storage, parameters.producerTime(), threadId++));
                producer.start();
            }
            for (int i = 0; i < parameters.consumerCount(); i++) {
                Thread consumer = new Thread(new Consumer(storage, parameters.consumerTime(), threadId++));
                consumer.start();
            }
        } catch (AppException e) {
            log.error(e.getMessage(), e);
        }
    }
}
