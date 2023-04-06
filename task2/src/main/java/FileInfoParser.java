import factory.ShapeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class FileInfoParser {
    private static final String PARAMETER_IS_NOT_A_NUMBER_MESSAGE =
            "Один или несколько параметров фигуры не являются числами";
    private static final String PARAMETER_IS_NEGATIVE_NUMBER_MESSAGE =
            "Один или несколько параметров фигуры являются отрицательными числами";
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    private final ShapeType shapeType;
    private final List<Double> parameters;

    public ShapeType getShapeType() {
        return shapeType;
    }

    public List<Double> getParameters() {
        return parameters;
    }

    public FileInfoParser(FileContent fileContent) {
        this.parameters = toShapeParameters(fileContent.parameters());
        this.shapeType = ShapeType.getByName(fileContent.firstLine());
    }

    private static List<Double> toShapeParameters(List<String> parameters) {
        try {
            return parameters.stream()
                    .map(p -> {
                        double curDouble = Double.parseDouble(p);
                        if (curDouble < 0) {
                            log.error(PARAMETER_IS_NEGATIVE_NUMBER_MESSAGE + ": " + curDouble);
                            throw new IllegalArgumentException(PARAMETER_IS_NEGATIVE_NUMBER_MESSAGE);
                        }
                        return curDouble;
                    })
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            log.error(PARAMETER_IS_NOT_A_NUMBER_MESSAGE + ". " + e.getMessage() + "\n" + e);
            throw new IllegalArgumentException(PARAMETER_IS_NOT_A_NUMBER_MESSAGE, e);
        }
    }
}
