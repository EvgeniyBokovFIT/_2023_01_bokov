import exception.FileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private static final String SHAPE_CHARACTERISTICS_PRINTED_TO_CONSOLE =
            "Характеристики фигуры выведены в консоль";
    private static final String SHAPE_CHARACTERISTICS_WRITTEN_TO_FILE =
            "Характеристики фигуры записаны в файл ";
    private static final String FILE_PROCESSING_ERROR =
            "Ошибка работы с выходным файлом ";
    private static final Logger log = LoggerFactory.getLogger(Writer.class);

    public void writeInfo(OutputType outputType, String outputContent, String filename) {
        if (outputType.equals(OutputType.CONSOLE)) {
            System.out.print(outputContent);
            log.info(outputContent);
            log.info(SHAPE_CHARACTERISTICS_PRINTED_TO_CONSOLE);
        } else {
            writeToFile(filename, outputContent);
            log.info(SHAPE_CHARACTERISTICS_WRITTEN_TO_FILE + filename);
        }
    }

    private void writeToFile(String filename, String outputContent) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(outputContent);
        } catch (IOException e) {
            log.error(FILE_PROCESSING_ERROR + filename + ". " + e.getMessage() + "\n" + e);
            throw new FileException(FILE_PROCESSING_ERROR, filename, e);
        }
    }
}
