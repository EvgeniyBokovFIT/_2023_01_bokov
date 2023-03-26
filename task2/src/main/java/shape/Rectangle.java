package shape;

import exception.ShapeException;

import java.text.DecimalFormat;

public class Rectangle extends Shape{
    public static final Integer ARGS_COUNT = 2;

    private final Double length;
    private final Double width;

    public Rectangle(Double firstSide, Double secondSide) throws ShapeException {
        super("Прямоугольник");
        if(firstSide < 0 || secondSide < 0) {
            throw new ShapeException(NEGATIVE_ARGS_MESSAGE);
        }
        this.length = Math.max(firstSide, secondSide);
        this.width = Math.min(firstSide, secondSide);
        this.area = this.length * this.width;
        this.perimeter = 2 * (this.length + this.width);
    }

    @Override
    protected String getSpecialCharacteristics() {
        DecimalFormat format = new DecimalFormat(FORMAT_PATTERN);

        return String.format(
                """
                        Длина диагонали: %s %s
                        Длина: %s %s
                        Ширина: %s %s
                        """,
                format.format(Math.sqrt(this.length * this.length + this.width * this.width)), UNITS,
                format.format(this.length), UNITS,
                format.format(this.width), UNITS);
    }
}
