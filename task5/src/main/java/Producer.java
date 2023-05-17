import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer implements Runnable {
    private static final String THREAD_TYPE_MESSAGE = "Тип потока: Производитель.";
    private static int resourceIdSequence = 1;
    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private final Storage storage;
    private final long produceTimeMillis;
    private final int id;
    private boolean running = true;

    public Producer(Storage storage, long produceTimeMillis, int id) {
        this.storage = storage;
        this.produceTimeMillis = produceTimeMillis;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            produce();
        } catch (InterruptedException e) {
            log.info(THREAD_TYPE_MESSAGE + " Id потока: {}. Поток прерван", this.id);
        }
    }

    public void produce() throws InterruptedException {
        while (this.running) {
            synchronized (this.storage) {
                while (this.storage.isFull()) {
                    log.debug(THREAD_TYPE_MESSAGE + " Id потока: {}. Режим ожидания.", this.id);
                    this.storage.wait();
                    log.debug(THREAD_TYPE_MESSAGE + " Id потока: {}. Пробуждение", this.id);
                }
                produceResource();
            }
            Thread.sleep(this.produceTimeMillis);
        }
    }

    private void produceResource() {
        Resource resource = new Resource(createNextResourceId());
        this.storage.add(resource);
        log.info(THREAD_TYPE_MESSAGE + " Id потока: {}. Ресурс произведен. Id ресурса: {}. Ресурсов на складе: {}",
                this.id, resource.id(), this.storage.size());
        this.storage.notifyAll();
    }

    private static int createNextResourceId() {
        synchronized (Producer.class) {
            return resourceIdSequence++;
        }
    }

    public void stop() {
        this.running = false;
    }
}
