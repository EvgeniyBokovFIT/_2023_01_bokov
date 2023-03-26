import exception.FileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private static final Logger log = LoggerFactory.getLogger(Writer.class);

    public void writeInfo(OutputType outputType, String outputContent, String filename) {
        if (outputType.equals(OutputType.CONSOLE)) {
            System.out.print(outputContent);
            log.info("Характеристики фигуры выведены в консоль");
        } else {
            writeToFile(filename, outputContent);
            log.info("Характеристики фигуры записаны в файл " + filename);
        }
    }

    private void writeToFile(String filename, String outputContent) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(outputContent);
        } catch (IOException e) {
            final String fileErrorMessage = "Ошибка работы с выходным файлом " + filename;
            log.error(fileErrorMessage + ". " + e.getMessage() + "\n" + e);
            throw new FileException(fileErrorMessage, e);
        }
    }
}
