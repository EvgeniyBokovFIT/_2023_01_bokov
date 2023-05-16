import java.util.concurrent.Callable;

public abstract class SeriesSumTask implements Callable<Double> {
    protected int startIndex;
    protected int sliceSize;
    protected double result;

    public SeriesSumTask(int startIndex, int sliceSize) {
        this.startIndex = startIndex;
        this.sliceSize = sliceSize;
    }

    @Override
    public abstract Double call();
}
