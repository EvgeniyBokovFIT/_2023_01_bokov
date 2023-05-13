import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadSeriesSumCalculator {
    private MultiThreadSeriesSumCalculator() {

    }

    public static double calculateSeriesSum(int startIndex, int endIndex)
            throws ExecutionException, InterruptedException {
        int threadsCount = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(threadsCount);

        int iterationsCount = endIndex - startIndex + 1;
        int minRangeSize = iterationsCount / threadsCount;

        List<Future<Double>> futures = new ArrayList<>();
        int nextThreadStartIndex = startIndex;
        for (int i = 0; i < threadsCount; i++) {
            int rangeSize = i < (iterationsCount % threadsCount) ? minRangeSize + 1 : minRangeSize;
            SeriesSumTask task = new MyTask(nextThreadStartIndex, rangeSize);
            nextThreadStartIndex += rangeSize;
            futures.add(threadPool.submit(task));
        }
        threadPool.shutdown();

        double seriesSum = 0;
        for (Future<Double> future : futures) {
            seriesSum += future.get();
        }
        return seriesSum;
    }
}
