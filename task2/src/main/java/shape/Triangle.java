package shape;

import exception.FigureException;

import java.text.DecimalFormat;

public class Triangle extends Shape{
    private static final String ANGLE_UNITS = "градусов";
    public static final Integer ARGS_COUNT = 3;

    private final double firstSide;
    private final double secondSide;
    private final double thirdSide;

    public Triangle(Double firstSide, Double secondSide, Double thirdSide) throws FigureException {
        super("Треугольник");
        if(firstSide < 0 || secondSide < 0 || thirdSide < 0) {
            throw new FigureException(NEGATIVE_ARGS_MESSAGE);
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
        double angleInRadians = Math.acos(
                (square(secondSide) + square(thirdSide) - square(firstSide)) / (2 * secondSide * thirdSide));
        return radiansToDegrees(angleInRadians);
    }

    private Double square(Double number) {
        return number * number;
    }

    private Double radiansToDegrees(Double radians) {
        return radians * 180 / Math.PI;
    }
}
