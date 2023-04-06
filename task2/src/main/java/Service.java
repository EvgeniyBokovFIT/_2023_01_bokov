import exception.FileException;
import exception.ShapeException;
import factory.ShapeFactory;
import factory.ShapeType;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shape.Shape;

import java.util.List;

public class Service {
    private static final String FILE_READ_SUCCESSFULLY_MESSAGE =
            "Входной файл успешно прочитан. Название файла: \"{}\"";
    private static final String PARSING_COMPLETED_SUCCESSFULLY_MESSAGE =
            "Парсинг информации из входного файла выполнен успешно. Тип фигуры: {}. Параметры: {}";
    private static final String COMMAND_LINE_PARSING_FAILURE_MESSAGE =
            "Ошибка парсинга командной строки. ";
    private static final String SHAPE_CREATED_SUCCESSFULLY_MESSAGE =
            "Фигура успешно создана." + "Введенный тип фигуры: {} ." + "Введенные параметры: {}";
    private static final Logger log = LoggerFactory.getLogger(Service.class);
    private final ShapeFactory shapeFactory = new ShapeFactory();

    public void doWork(String[] args) {
        try {
            CommandLineArgumentsParser commandLineParser = new CommandLineArgumentsParser(args);

            String inputFilename = commandLineParser.getInputFilename();
            FileContent fileContent = FileReader.read(inputFilename);
            log.info(FILE_READ_SUCCESSFULLY_MESSAGE, inputFilename);

            FileInfoParser fileInfoParser = new FileInfoParser(fileContent);
            ShapeType shapeType = fileInfoParser.getShapeType();
            List<Double> shapeParameters = fileInfoParser.getParameters();
            log.info(PARSING_COMPLETED_SUCCESSFULLY_MESSAGE,
                    shapeType, shapeParameters);

            Shape shape = shapeFactory.getShape(shapeType, shapeParameters);
            log.info(SHAPE_CREATED_SUCCESSFULLY_MESSAGE,
                    shapeType, shapeParameters);
            String output = shape.getShapeCharacteristics();
            Writer writer = new Writer();
            writer.writeInfo(commandLineParser.getOutputType(), output, commandLineParser.getOutputFilename());
        } catch (ShapeException | FileException | IllegalArgumentException e) {
            log.error(e.getMessage() + "\n" + e);
        } catch (ParseException e) {
            log.error(COMMAND_LINE_PARSING_FAILURE_MESSAGE + e.getMessage() + "\n" + e);
        }
    }
}
