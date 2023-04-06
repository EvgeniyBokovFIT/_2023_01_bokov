package shape;

import exception.ShapeException;

import java.text.DecimalFormat;

public class Circle extends Shape {
    public static final Integer ARGS_COUNT = 1;

    private final Double radius;

    public Circle(Double radius) throws ShapeException {
        super("Круг");
        if(radius < 0) {
            throw new ShapeException(NEGATIVE_ARGS_MESSAGE);
        }
        this.radius = radius;
        this.area = Math.PI * radius * radius;
        this.perimeter = 2 * Math.PI * radius;
    }

    @Override
    protected String getSpecialCharacteristics() {
        DecimalFormat format = new DecimalFormat(FORMAT_PATTERN);

        return String.format(
                """
                        Радиус: %s %s
                        Диаметр: %s %s
                        """,
                format.format(this.radius), UNITS,
                format.format(this.radius * 2), UNITS);
    }
}
