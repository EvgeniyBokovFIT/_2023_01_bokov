import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer implements Runnable {
    private static final String THREAD_TYPE_MESSAGE = "Тип потока: Производитель.";
    private static int resourceIdSequence = 1;
    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private final Storage storage;
    private final long produceTimeMillis;
    private final int id;

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
            log.error(e.getMessage(), e);
        }
    }

    public void produce() throws InterruptedException {
        while (true) {
            while (this.storage.isFull()) {
                this.storage.notifyForEmpty();
                log.debug(THREAD_TYPE_MESSAGE + " Id потока: {}. Режим ожидания.", this.id);
                this.storage.waitOnFull();
                log.debug(THREAD_TYPE_MESSAGE + " Id потока: {}. Пробуждение", this.id);
            }
            produceResource();
        }
    }

    private void produceResource() throws InterruptedException {
        synchronized (this.storage) {
            if (!this.storage.isFull()) {
                Resource resource = new Resource(createNextResourceId());
                this.storage.add(resource);
                log.info(THREAD_TYPE_MESSAGE + " Id потока: {}. Ресурс произведен. Id ресурса: {}. Ресурсов на складе: {}",
                        this.id, resource.id(), this.storage.size());
                this.storage.notifyForEmpty();
            }
        }
        Thread.sleep(this.produceTimeMillis);
    }

    private static int createNextResourceId() {
        synchronized (Producer.class) {
            return resourceIdSequence++;
        }
    }
}
