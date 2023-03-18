public class Validator {
    public static final Integer MIN_SIZE_VALUE = 1;
    public static final Integer MAX_SIZE_VALUE = 32;

    private Validator() {

    }

    public static boolean isValidSizeValue(int value) {
        return value >= MIN_SIZE_VALUE && value <= MAX_SIZE_VALUE;
    }
}
