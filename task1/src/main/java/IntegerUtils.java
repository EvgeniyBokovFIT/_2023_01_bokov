public class IntegerUtils {

    private IntegerUtils() {

    }

    public static int getNumberOfDigits(int number) {
        return Integer.toString(number).length();
    }
}
