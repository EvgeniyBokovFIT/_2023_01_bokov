import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTask extends SeriesSumTask {
    private static final Logger log = LoggerFactory.getLogger(MyTask.class);

    public MyTask(int startIndex, int sliceSize) {
        super(startIndex, sliceSize);
    }

    @Override
    public Double call() {
        for (int i = this.startIndex; i < this.startIndex + this.sliceSize; i++) {
            this.result += Math.pow(2, -i);
        }
        log.debug("Сумма в данном потоке: {}", this.result);
        return this.result;
    }
}
