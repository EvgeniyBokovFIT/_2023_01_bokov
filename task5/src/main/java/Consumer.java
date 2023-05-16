import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer implements Runnable {
    private static final String THREAD_TYPE_MESSAGE = "Тип потока: Потребитель.";
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    private final Storage storage;
    private final long consumeTime;
    private final int id;

    public Consumer(Storage storage, long consumeTime, int id) {
        this.storage = storage;
        this.consumeTime = consumeTime;
        this.id = id;
    }

    public void consume() throws InterruptedException {
        while (true) {
            while (this.storage.isEmpty()) {
                this.storage.notifyForFull();
                log.debug(THREAD_TYPE_MESSAGE + " Id потока: {}. Режим ожидания", this.id);
                this.storage.waitOnEmpty();
                log.debug(THREAD_TYPE_MESSAGE + " Id потока: {}. Пробуждение", this.id);
            }
            consumeResource();
        }
    }

    private void consumeResource() throws InterruptedException {
        synchronized (this.storage) {
            if (!this.storage.isEmpty()) {
                Resource resource = this.storage.remove();
                log.info(THREAD_TYPE_MESSAGE + " Id потока: {}. Ресурс потреблен. Id ресурса: {}. Ресурсов на складе: {}",
                        this.id, resource.id(), this.storage.size());
                this.storage.notifyForFull();
            }
        }
        Thread.sleep(this.consumeTime);
    }

    @Override
    public void run() {
        try {
            consume();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
