import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer implements Runnable {
    private static final String THREAD_TYPE_MESSAGE = "Тип потока: Потребитель.";
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    private final Storage storage;
    private final long consumeTime;
    private final int id;
    private boolean running = true;

    public Consumer(Storage storage, long consumeTime, int id) {
        this.storage = storage;
        this.consumeTime = consumeTime;
        this.id = id;
    }

    public void consume() throws InterruptedException {
        while (this.running) {
            synchronized (this.storage) {
                while (this.storage.isEmpty()) {
                    log.debug(THREAD_TYPE_MESSAGE + " Id потока: {}. Режим ожидания", this.id);
                    this.storage.wait();
                    log.debug(THREAD_TYPE_MESSAGE + " Id потока: {}. Пробуждение", this.id);
                }
                consumeResource();
            }
            Thread.sleep(this.consumeTime);
        }
    }

    private void consumeResource() {
        Resource resource = this.storage.remove();
        log.info(THREAD_TYPE_MESSAGE + " Id потока: {}. Ресурс потреблен. Id ресурса: {}. Ресурсов на складе: {}",
                this.id, resource.id(), this.storage.size());
        this.storage.notifyAll();
    }

    @Override
    public void run() {
        try {
            consume();
        } catch (InterruptedException e) {
            log.info(THREAD_TYPE_MESSAGE + " Id потока: {}. Поток прерван", this.id);
        }
    }

    public void stop() {
        this.running = false;
    }
}
