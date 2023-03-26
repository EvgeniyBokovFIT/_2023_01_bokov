package factory;

import exception.ShapeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shape.Circle;
import shape.Rectangle;
import shape.Shape;
import shape.Triangle;

import java.util.List;

public class ShapeFactory {
    private static final Logger log = LoggerFactory.getLogger(ShapeFactory.class);
    private final static String INVALID_ARGS_COUNT_MESSAGE = "Неверное количество параметров фигуры";

    public Shape getShape(ShapeType shapeType, List<Double> shapeParameters) {
        int parametersCount = shapeParameters.size();
        if (shapeType.equals(ShapeType.CIRCLE) && Circle.ARGS_COUNT.equals(parametersCount)) {
            return new Circle(shapeParameters.get(0));
        }

        if (shapeType.equals(ShapeType.RECTANGLE) && Rectangle.ARGS_COUNT.equals(parametersCount)) {
            return new Rectangle(shapeParameters.get(0), shapeParameters.get(1));
        }

        if (shapeType.equals(ShapeType.TRIANGLE) && Triangle.ARGS_COUNT.equals(parametersCount)) {
            return new Triangle(shapeParameters.get(0), shapeParameters.get(1), shapeParameters.get(2));
        }

        log.error(INVALID_ARGS_COUNT_MESSAGE +
                ". Введено параметров: {}" + ". Тип фигуры: {}",
                parametersCount, shapeType);
        throw new ShapeException(INVALID_ARGS_COUNT_MESSAGE);
    }
}
