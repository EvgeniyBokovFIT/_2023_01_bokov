import exception.ShapeException;
import factory.ShapeFactory;
import factory.ShapeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import shape.Circle;
import shape.Shape;

import java.util.List;

public class ShapeTest {
    ShapeFactory factory = new ShapeFactory();
    @Test
    void CircleTest() {
        String expected = """
                Тип фигуры: Круг
                Площадь: 78,54 кв. мм
                Периметр: 31,42 мм
                Радиус: 5 мм
                Диаметр: 10 мм
                """;

        Shape shape = factory.getShape(ShapeType.CIRCLE, List.of(5.D));
        String shapeCharacteristics = shape.getShapeCharacteristics();

        Assertions.assertEquals(expected, shapeCharacteristics);
    }

    @Test
    void RectangleTest() {
        String expected = """
                Тип фигуры: Прямоугольник
                Площадь: 35 кв. мм
                Периметр: 24 мм
                Длина диагонали: 8,6 мм
                Длина: 7 мм
                Ширина: 5 мм
                """;

        Shape shape = factory.getShape(ShapeType.RECTANGLE, List.of(5.D, 7.D));
        String shapeCharacteristics = shape.getShapeCharacteristics();

        Assertions.assertEquals(expected, shapeCharacteristics);
    }

    @Test
    void TriangleTest() {
        String expected = """
                Тип фигуры: Треугольник
                Площадь: 6 кв. мм
                Периметр: 12 мм
                Длина первой стороны: 3 мм
                Противолежащий угол первой стороны: 36,87 градусов
                Длина второй стороны: 4 мм
                Противолежащий угол второй стороны: 53,13 градусов
                Длина третьей стороны: 5 мм
                Противолежащий угол третьей стороны: 90 градусов
                """;

        Shape shape = factory.getShape(ShapeType.TRIANGLE, List.of(3.D, 4.D, 5.D));
        String shapeCharacteristics = shape.getShapeCharacteristics();

        Assertions.assertEquals(expected, shapeCharacteristics);
    }

    @Test
    void factoryTest() {
        Double radius = 100.D;
        Shape expected = new Circle(radius);

        Shape shape = factory.getShape(ShapeType.CIRCLE, List.of(radius));

        Assertions.assertEquals(expected.getShapeCharacteristics(), shape.getShapeCharacteristics());
    }
    @Test
    void invalidArgsCountTest() {
        Assertions.assertThrows(ShapeException.class, () ->
                factory.getShape(ShapeType.CIRCLE, List.of(9.D, 8.D)));
    }

    @Test
    void negativeArgsTest() {
        Assertions.assertThrows(ShapeException.class, () ->
                new Circle(-1.D));
    }
}
