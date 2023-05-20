package serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import message.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Serializer {
    private final ObjectMapper objectMapper;

    public Serializer() {
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        this.objectMapper = new ObjectMapper(jsonFactory);
    }

    public Message readMessage(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, Message.class);
    }

    public void writeMessage(OutputStream outputStream, Message message) throws IOException {
        objectMapper.writeValue(outputStream, message);
    }
}
