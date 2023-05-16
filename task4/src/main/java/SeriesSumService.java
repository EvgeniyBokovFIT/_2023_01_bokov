import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class SeriesSumService {
    private static final String INFO_MESSAGE = "Введите неотрицательное число N";

    private static final Logger log = LoggerFactory.getLogger(SeriesSumService.class);

    private SeriesSumService() {

    }

    public static double calculateSeriesSum() throws AppException {
        int n = readLastIndex();
        try {
            return MultiThreadSeriesSumCalculator.calculateSeriesSum(0, n);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new AppException(e.getMessage(), e);
        }
    }

    private static int getNextInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            log.error("Введенные данные не являются целым числом. Введено: {}", scanner.next());
            System.out.println(INFO_MESSAGE);
        }

        return scanner.nextInt();
    }

    private static int readLastIndex() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(INFO_MESSAGE);

        int n = getNextInt(scanner);
        while (n < 0) {
            log.error("Введенное число является отрицательным. Введено число: {}", n);
            System.out.println(INFO_MESSAGE);
            n = getNextInt(scanner);
        }
        scanner.close();
        return n;
    }
}
