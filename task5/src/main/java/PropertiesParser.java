import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesParser {
    private static final String PROPERTIES_FILENAME = "config.properties";
    private static final String IO_ERROR_MESSAGE = "Ошибка работы с файлом " + PROPERTIES_FILENAME;
    private static final String PROPERTIES_ERROR_MESSAGE =
            "Не указано или неверно указано одно из свойств в файле " + PROPERTIES_FILENAME;
    private static final Logger log = LoggerFactory.getLogger(PropertiesParser.class);

    public static Parameters parse() throws AppException {
        try (InputStream propertiesInputStream = new FileInputStream("task5/src/main/resources/" + PROPERTIES_FILENAME)) {
            Properties properties = new Properties();
            properties.load(propertiesInputStream);

            return new Parameters(Integer.parseInt(properties.getProperty("producerCount")),
                    Integer.parseInt(properties.getProperty("consumerCount")),
                    Integer.parseInt(properties.getProperty("producerTime")),
                    Integer.parseInt(properties.getProperty("consumerTime")),
                    Integer.parseInt(properties.getProperty("storageSize")));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new AppException(IO_ERROR_MESSAGE, e);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            throw new AppException(PROPERTIES_ERROR_MESSAGE, e);
        }
    }
}
