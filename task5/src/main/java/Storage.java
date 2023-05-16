import java.util.ArrayDeque;
import java.util.Queue;

public class Storage {
    private final Queue<Resource> resources = new ArrayDeque<>();
    private final Object fullStorageSynchronizer = new Object();
    private final Object emptyStorageSynchronizer = new Object();
    private final int maxSize;

    public Storage(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isEmpty() {
        return this.resources.isEmpty();
    }

    public boolean isFull() {
        return this.maxSize == resources.size();
    }

    public void add(Resource resource) {
        synchronized (this.resources) {
            this.resources.add(resource);
        }
    }

    public Resource remove() {
        synchronized (this.resources) {
            return this.resources.poll();
        }
    }

    public void waitOnEmpty() throws InterruptedException {
        synchronized (this.emptyStorageSynchronizer) {
            this.emptyStorageSynchronizer.wait();
        }
    }

    public void waitOnFull() throws InterruptedException {
        synchronized (this.fullStorageSynchronizer) {
            this.fullStorageSynchronizer.wait();
        }
    }

    public void notifyForEmpty() {
        synchronized (this.emptyStorageSynchronizer) {
            this.emptyStorageSynchronizer.notify();
        }
    }

    public void notifyForFull() {
        synchronized (this.fullStorageSynchronizer) {
            this.fullStorageSynchronizer.notify();
        }
    }

    public int size() {
        return this.resources.size();
    }
}
