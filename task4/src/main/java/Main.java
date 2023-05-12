import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            double result = SeriesSumService.calculateSeriesSum();
            log.info("Сумма ряда: {}", result);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
