import factory.ShapeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
        this.parameters = toShapeParameters(fileContent.getParameters());
        this.shapeType = ShapeType.getByName(fileContent.getFirstLine());
    }

    private static List<Double> toShapeParameters(List<String> parameters) {
        List<Double> shapeParameters = new ArrayList<>();
        try {
            parameters.forEach(p -> addDoubleToList(shapeParameters, Double.parseDouble(p)));
        } catch (NumberFormatException e) {
            log.error(PARAMETER_IS_NOT_A_NUMBER_MESSAGE + ". " + e.getMessage() + "\n" + e);
            throw new IllegalArgumentException(PARAMETER_IS_NOT_A_NUMBER_MESSAGE, e);
        }
        return shapeParameters;
    }

    private static void addDoubleToList(List<Double> shapeParameters, Double curDouble) {
        if(curDouble < 0) {
            log.error(PARAMETER_IS_NEGATIVE_NUMBER_MESSAGE + ": " + curDouble);
            throw new IllegalArgumentException(PARAMETER_IS_NEGATIVE_NUMBER_MESSAGE);
        }
        shapeParameters.add(curDouble);
    }
}
