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
    private static final String FILE_PROCESSING_ERROR = "Ошибка работы с файлом ";
    private static final String TOO_FEW_LINES_MESSAGE = "Слишком мало строк во входном файле ";
    private static final String TOO_MANY_LINES_MESSAGE = "Слишком много строк во входном файле ";
    private static final String FILE_IS_EMPTY_MESSAGE = "Входной файл пуст ";
    private static final Logger log = LoggerFactory.getLogger(FileReader.class);

    private FileReader() {

    }

    public static FileContent read(String filename) throws FileException {
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             Scanner scanner = new Scanner(fileInputStream)){

            if(!scanner.hasNextLine()) {
                log.error(FILE_IS_EMPTY_MESSAGE + filename);
                throw new FileException(FILE_IS_EMPTY_MESSAGE, filename);
            }
            FileContent fileContent = new FileContent(scanner.nextLine(), readParameters(scanner, filename));

            if(scanner.hasNextLine()) {
                log.error(TOO_MANY_LINES_MESSAGE + filename);
                throw new FileException(TOO_MANY_LINES_MESSAGE, filename);
            }
            return fileContent;
        } catch (IOException e) {
            log.error(FILE_PROCESSING_ERROR + filename + ". " + e.getMessage() + "\n" + e);
            throw new FileException(FILE_PROCESSING_ERROR, filename, e);
        }
    }

    private static List<String> readParameters(Scanner scanner, String filename) throws FileException {
        if(!scanner.hasNextLine()) {
            log.error(TOO_FEW_LINES_MESSAGE + filename);
            throw new FileException(TOO_FEW_LINES_MESSAGE, filename);
        }
        String nextLine = scanner.nextLine();
        String[] parameters = nextLine.split(FILE_SEPARATOR);
        return Arrays.asList(parameters);
    }
}
