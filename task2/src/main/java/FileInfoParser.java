import factory.ShapeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FileInfoParser {
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    private ShapeType shapeType;
    private List<Double> parameters;

    public ShapeType getShapeType() {
        return shapeType;
    }

    public List<Double> getParameters() {
        return parameters;
    }

    public void parse(String firstLine, List<String> parameters) {
        this.parameters = toShapeParameters(parameters);
        this.shapeType = ShapeType.getByName(firstLine);
    }

    private List<Double> toShapeParameters(List<String> parameters) {
        List<Double> doubles = new ArrayList<>();
        try {
            for (var parameter: parameters) {
                double curDouble = Double.parseDouble(parameter);
                addDoubleToList(doubles, curDouble);
            }
        } catch (NumberFormatException e) {
            log.error("Параметр фигуры не является числом. " + e.getMessage() + "\n" + e);
            throw new IllegalArgumentException("Один или несколько параметров фигуры не являются числами", e);
        }
        return doubles;
    }

    private void addDoubleToList(List<Double> shapeParameters, double curDouble) {
        if(curDouble < 0) {
            log.error("Неверный параметр фигуры: \"{}\"", curDouble);
            throw new IllegalArgumentException(
                    "Один или несколько параметров фигуры являются отрицательными числами");
        }
        shapeParameters.add(curDouble);
    }
}
