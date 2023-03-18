import java.util.Scanner;

public class Main {
    private static final String INFO_MESSAGE = "Введите целое число от " + Validator.MIN_SIZE_VALUE +
            " до " + Validator.MAX_SIZE_VALUE;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(INFO_MESSAGE);
        int tableSize = getNextInt(scanner);

        while (!Validator.isValidSizeValue(tableSize)) {
            System.err.println("Введенное число не принадлежит диапазону от " + Validator.MIN_SIZE_VALUE +
                    " до " + Validator.MAX_SIZE_VALUE);
            System.out.println(INFO_MESSAGE);

            tableSize = getNextInt(scanner);
        }

        scanner.close();

        MultiplicationTablePrinter printer = new MultiplicationTablePrinter();
        printer.printMultiplicationTable(tableSize);
    }

    private static int getNextInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.err.println("Введенные данные не являются целым числом");
            System.out.println(INFO_MESSAGE);

            scanner.next();
        }

        return scanner.nextInt();
    }

}
