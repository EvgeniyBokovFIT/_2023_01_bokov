import java.util.ArrayDeque;
import java.util.Queue;

public class Storage {
    private final Queue<Resource> resources = new ArrayDeque<>();
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

    public int size() {
        return this.resources.size();
    }
}
