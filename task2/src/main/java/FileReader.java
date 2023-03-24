import exception.FileException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private static final String FILE_SEPARATOR = " ";
    private static final String INVALID_FORMAT_MESSAGE = "Неверный формат файла";

    private String firstLine;
    private List<String> parameters;

    public void read(String filename) throws FileException {
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             Scanner scanner = new Scanner(fileInputStream)){

            if(!scanner.hasNextLine()) {
                throw new FileException(INVALID_FORMAT_MESSAGE);
            }
            firstLine = scanner.nextLine();

            readParameters(scanner);
        } catch (IOException e) {
            throw new FileException("Ошибка работы с файлом " + filename);
        }
    }

    private void readParameters(Scanner scanner) throws FileException {
        if(!scanner.hasNextLine()) {
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
