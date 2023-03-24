import exception.FigureException;
import exception.FileException;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shape.Shape;
import shape.ShapeFactory;
import shape.ShapeType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    public void doWork(String[] args) {
        try {
            ArgumentsParser parser = new ArgumentsParser();
            parser.parse(args);
            log.info("Парсинг аргументов командной строки выполнен успешно");

            FileReader fileReader = new FileReader();
            fileReader.read(parser.getInputFilename());
            log.info("Входной файл успешно прочитан");

            ShapeFactory shapeFactory = new ShapeFactory();
            Shape shape = shapeFactory.getShape(ShapeType.valueOf(fileReader.getFirstLine()),
                    toDoubleList(fileReader.getParameters()));
            log.info("Фигура успешно создана");

            String output = shape.getShapeCharacteristics();
            writeInfo(parser.getOutputType(), output, parser.getOutputFilename());
        } catch (FigureException | FileException e) {
            log.error(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Неверно указаны тип или параметры фигуры");
        } catch (ParseException e) {
            log.error("Ошибка парсинга командной строки");
        }
    }

    private void writeToFile(String filename, String outputContent) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(outputContent);
        } catch (IOException e) {
            throw new FileException("Ошибка работы с файлом " + filename, e);
        }
    }

    private List<Double> toDoubleList(List<String> strings) {
        List<Double> doubles = new ArrayList<>();
        try {
            for (var string: strings) {
                doubles.add(Double.parseDouble(string));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверно заданы параметры фигуры");
        }
        return doubles;
    }

    private void writeInfo(OutputType outputType, String outputContent, String filename) {
        if (outputType.equals(OutputType.Console)) {
            System.out.print(outputContent);
            log.info("информация о фигуре выведена в консоль");
        } else {
            writeToFile(filename, outputContent);
            log.info("информация о фигуре записана в файл " + filename);
        }
    }
}
