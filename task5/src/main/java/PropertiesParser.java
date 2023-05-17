import exception.PropertiesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesParser {
    private static final String PROPERTIES_FILENAME = "config.properties";
    private static final String IO_ERROR_MESSAGE = "Ошибка работы с файлом " + PROPERTIES_FILENAME;

    private static final Logger log = LoggerFactory.getLogger(PropertiesParser.class);

    public static Parameters parse() throws PropertiesException {
        try (InputStream propertiesInputStream = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILENAME)) {
            Properties properties = new Properties();
            properties.load(propertiesInputStream);

            int producerCount = parsePropertyIntValue(properties, "producerCount");

            int consumerCount = parsePropertyIntValue(properties, "consumerCount");

            int producerTime = parsePropertyIntValue(properties, "producerTime");

            int consumerTime = parsePropertyIntValue(properties, "consumerTime");

            int storageSize = parsePropertyIntValue(properties, "storageSize");

            return new Parameters(producerCount, consumerCount, producerTime, consumerTime, storageSize);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new PropertiesException(IO_ERROR_MESSAGE, e);
        }
    }

    private static int parsePropertyIntValue(Properties properties, String propertyName) throws PropertiesException {
        String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            log.error("Не задан параметр " + propertyName);
            throw new PropertiesException("Не задан параметр " + propertyName);
        }
        try {
            return Integer.parseInt(propertyValue);
        } catch (NumberFormatException e) {
            log.error("Не верно указан параметр " + propertyName);
            throw new PropertiesException("Не верно указан параметр " + propertyName);
        }
    }
}
