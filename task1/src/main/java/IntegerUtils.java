public class IntegerUtils {

    private IntegerUtils() {

    }

    public static int getNumberOfDigits(int number) {
        int digitsNumber = 1;

        while (number >= 10) {
            digitsNumber++;
            number/=10;
        }

        return digitsNumber;
    }
}
