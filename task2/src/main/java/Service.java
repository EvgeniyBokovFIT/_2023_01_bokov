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
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    public void doWork(String[] args) {
        try {
            CommandLineArgumentsParser commandLineParser = new CommandLineArgumentsParser();
            commandLineParser.parse(args);

            FileReader fileReader = new FileReader();
            String inputFilename = commandLineParser.getInputFilename();
            fileReader.read(inputFilename);
            log.info("Входной файл успешно прочитан. Название файла: \"{}\"", inputFilename);

            FileInfoParser fileInfoParser = new FileInfoParser();
            fileInfoParser.parse(fileReader.getFirstLine(), fileReader.getParameters());
            ShapeType shapeType = fileInfoParser.getShapeType();
            List<Double> shapeParameters = fileInfoParser.getParameters();
            log.info("Парсинг информации из входного файла выполнен успешно. Тип фигуры: {}. Параметры: {}",
                    shapeType, shapeParameters);

            ShapeFactory shapeFactory = new ShapeFactory();
            Shape shape = shapeFactory.getShape(shapeType, shapeParameters);
            log.info("Фигура успешно создана." + "Введенный тип фигуры: {} ." + "Введенные параметры: {}",
                    shapeType, shapeParameters);
            String output = shape.getShapeCharacteristics();
            Writer writer = new Writer();
            writer.writeInfo(commandLineParser.getOutputType(), output, commandLineParser.getOutputFilename());
        } catch (ShapeException | FileException | IllegalArgumentException e) {
            log.error(e.getMessage() + "\n" + e);
        } catch (ParseException e) {
            log.error("Ошибка парсинга командной строки. " + e.getMessage() + "\n" + e);
        }
    }
}
