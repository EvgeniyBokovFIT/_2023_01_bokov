import exception.FileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private static final String FILE_SEPARATOR = " ";
    private static final String INVALID_FORMAT_MESSAGE = "Неверный формат файла";
    private static final String FILE_PROCESSING_ERROR = "Ошибка работы с файлом ";
    private static final Logger log = LoggerFactory.getLogger(FileReader.class);

    private String firstLine;
    private List<String> parameters;

    public void read(String filename) throws FileException {
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             Scanner scanner = new Scanner(fileInputStream)){

            if(!scanner.hasNextLine()) {
                log.error("Входной файл пуст");
                throw new FileException(INVALID_FORMAT_MESSAGE);
            }
            firstLine = scanner.nextLine();

            readParameters(scanner);

            if(scanner.hasNextLine()) {
                log.error("Слишком много строк во входном файле");
                throw new FileException(INVALID_FORMAT_MESSAGE);
            }
        } catch (IOException e) {
            log.error(FILE_PROCESSING_ERROR + filename + ". " + e.getMessage() + "\n" + e);
            throw new FileException(FILE_PROCESSING_ERROR + filename, e);
        }
    }

    private void readParameters(Scanner scanner) throws FileException {
        if(!scanner.hasNextLine()) {
            log.error("В файле слишком мало строк");
            throw new FileException(INVALID_FORMAT_MESSAGE);
        }
        String nextLine = scanner.nextLine();
        String[] parameters = nextLine.split(FILE_SEPARATOR);
        this.parameters = Arrays.asList(parameters);
    }

    public String getFirstLine() {
        return firstLine;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
