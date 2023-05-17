import exception.PropertiesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Service {
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    public static void startProduction() {
        try {
            int threadId = 1;
            Parameters parameters = PropertiesParser.parse();
            Storage storage = new Storage(parameters.storageSize());
            Map<Thread, Producer> threadProducerMap = new HashMap<>();
            Map<Thread, Consumer> threadConsumerMap = new HashMap<>();

            for (int i = 0; i < parameters.producerCount(); i++) {
                Producer producer = new Producer(storage, parameters.producerTime(), threadId++);
                Thread producerThread = new Thread(producer);
                threadProducerMap.put(producerThread, producer);
                producerThread.start();
            }
            for (int i = 0; i < parameters.consumerCount(); i++) {
                Consumer consumer = new Consumer(storage, parameters.consumerTime(), threadId++);
                Thread consumerThread = new Thread(consumer);
                threadConsumerMap.put(consumerThread, consumer);
                consumerThread.start();
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                for (Map.Entry<Thread, Producer> entry : threadProducerMap.entrySet()) {
                    try {
                        entry.getValue().stop();
                        entry.getKey().join();
                    } catch (InterruptedException e) {
                        log.info("Поток прерван");
                    }
                }
                for (Map.Entry<Thread, Consumer> entry : threadConsumerMap.entrySet()) {
                    try {
                        entry.getValue().stop();
                        entry.getKey().join();
                    } catch (InterruptedException e) {
                        log.info("Поток прерван");
                    }
                }
                log.info("HOOK END");
            }));
        } catch (PropertiesException e) {
            log.error(e.getMessage(), e);
        }
    }
}
