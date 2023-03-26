package shape;

import exception.ShapeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class Triangle extends Shape{
    private static final String ANGLE_UNITS = "градусов";
    public static final Integer ARGS_COUNT = 3;
    private static final Logger log = LoggerFactory.getLogger(Triangle.class);

    private final double firstSide;
    private final double secondSide;
    private final double thirdSide;

    public Triangle(Double firstSide, Double secondSide, Double thirdSide) throws ShapeException {
        super("Треугольник");
        if(firstSide < 0 || secondSide < 0 || thirdSide < 0) {
            throw new ShapeException(NEGATIVE_ARGS_MESSAGE);
        }
        this.firstSide = firstSide;
        this.secondSide = secondSide;
        this.thirdSide = thirdSide;

        this.perimeter = this.firstSide + this.secondSide + this.thirdSide;
        double semiPerimeter = this.perimeter / 2;
        this.area = Math.sqrt(semiPerimeter * (semiPerimeter - this.firstSide) *
                (semiPerimeter - this.secondSide) * (semiPerimeter - this.thirdSide));
    }

    @Override
    protected String getSpecialCharacteristics() {
        DecimalFormat format = new DecimalFormat(FORMAT_PATTERN);

        return String.format(
                """
                        Длина первой стороны: %s %s
                        Противолежащий угол первой стороны: %s %s
                        Длина второй стороны: %s %s
                        Противолежащий угол второй стороны: %s %s
                        Длина третьей стороны: %s %s
                        Противолежащий угол третьей стороны: %s %s
                        """,
                format.format(this.firstSide), UNITS,
                format.format(calculateAngleOppositeFirstSide(this.firstSide, this.secondSide, this.thirdSide)), ANGLE_UNITS,
                format.format(this.secondSide), UNITS,
                format.format(calculateAngleOppositeFirstSide(this.secondSide, this.firstSide, this.thirdSide)), ANGLE_UNITS,
                format.format(this.thirdSide), UNITS,
                format.format(calculateAngleOppositeFirstSide(this.thirdSide, this.firstSide, this.secondSide)), ANGLE_UNITS);
    }

    private Double calculateAngleOppositeFirstSide(Double firstSide, Double secondSide, Double thirdSide) {
        if(secondSide * thirdSide == 0) {
            return 0.D;
        }
        double cos = (square(secondSide) + square(thirdSide) - square(firstSide)) / (2 * secondSide * thirdSide);
        double angleInRadians = Math.acos(cos);
        log.debug("Угол в радианах: {}. Параметры: {}, {}, {}", angleInRadians, firstSide, secondSide, thirdSide);
        return Math.toDegrees(angleInRadians);
    }

    private Double square(Double number) {
        return number * number;
    }
}
