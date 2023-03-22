import java.util.Scanner;

public class Service {
    private static final Integer MIN_SIZE_VALUE = 1;
    private static final Integer MAX_SIZE_VALUE = 32;
    private static final String INFO_MESSAGE = "Введите целое число от " + MIN_SIZE_VALUE +
            " до " + MAX_SIZE_VALUE;

    public void doWork() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(INFO_MESSAGE);
        int tableSize = getNextInt(scanner);

        while (tableSize < MIN_SIZE_VALUE || tableSize > MAX_SIZE_VALUE) {
            System.err.println("Введенное число не принадлежит диапазону от " + MIN_SIZE_VALUE +
                    " до " + MAX_SIZE_VALUE);
            System.out.println(INFO_MESSAGE);

            tableSize = getNextInt(scanner);
        }

        scanner.close();

        MultiplicationTablePrinter printer = new MultiplicationTablePrinter();
        printer.printMultiplicationTable(tableSize);
    }

    private int getNextInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.err.println("Введенные данные не являются целым числом");
            System.out.println(INFO_MESSAGE);

            scanner.next();
        }

        return scanner.nextInt();
    }
}
