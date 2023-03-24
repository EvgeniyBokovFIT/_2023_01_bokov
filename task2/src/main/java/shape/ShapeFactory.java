package shape;

import exception.FigureException;

import java.util.List;

public class ShapeFactory {
    private final static String INVALID_ARGS_COUNT_MESSAGE = "Неверное количество параметров фигуры";

    public Shape getShape(ShapeType shapeType, List<Double> parameters) {
        if (shapeType.equals(ShapeType.CIRCLE) && Circle.ARGS_COUNT.equals(parameters.size())) {
            return new Circle(parameters.get(0));
        }

        if (shapeType.equals(ShapeType.RECTANGLE) && Rectangle.ARGS_COUNT.equals(parameters.size())) {
            return new Rectangle(parameters.get(0), parameters.get(1));
        }

        if (shapeType.equals(ShapeType.TRIANGLE) && Triangle.ARGS_COUNT.equals(parameters.size())) {
            return new Triangle(parameters.get(0), parameters.get(1), parameters.get(2));
        }

        throw new FigureException(INVALID_ARGS_COUNT_MESSAGE);
    }
}
