package shape;

import java.text.DecimalFormat;

public abstract class Shape {
    private static final Integer DECIMAL_PLACES = 2;
    protected static final String UNITS = "мм";
    protected static final String AREA_UNITS = "кв. мм";
    protected static final String FORMAT_PATTERN = "#." + "#".repeat(DECIMAL_PLACES);
    protected static final String NEGATIVE_ARGS_MESSAGE = "Параметры фигуры не могут принимать отрицательные значения";

    protected String name;
    protected Double area;
    protected Double perimeter;

    protected Shape(String name) {
        this.name = name;
    }

    public String getShapeCharacteristics() {
        DecimalFormat format = new DecimalFormat(FORMAT_PATTERN);
        String result = String.format(
                """
                        Тип фигуры: %s
                        Площадь: %s %s
                        Периметр: %s %s
                        """,
                this.name,
                format.format(this.area), AREA_UNITS,
                format.format(this.perimeter), UNITS);

        result += getSpecialCharacteristics();
        return result;
    }

    protected abstract String getSpecialCharacteristics();
}
