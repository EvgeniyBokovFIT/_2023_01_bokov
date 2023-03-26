package factory;

import exception.ShapeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ShapeType {
    CIRCLE,
    RECTANGLE,
    TRIANGLE;

    private static final Logger log = LoggerFactory.getLogger(ShapeType.class);

    public static ShapeType getByName(String name) {
        try {
            return ShapeType.valueOf(name);
        } catch (IllegalArgumentException e) {
            log.error("Неверный тип фигуры. Введенный параметр: \"{}\"", name);
            throw new ShapeException("Тип фигуры с таким именем не существует", e);
        }
    }
}
