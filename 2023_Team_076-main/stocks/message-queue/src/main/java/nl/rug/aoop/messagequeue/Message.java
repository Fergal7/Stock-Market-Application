package nl.rug.aoop.messagequeue;

import com.google.gson.*;
import lombok.Getter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *  Message with a header,body and timestamp.
 */
@Getter
public class Message {
    private final String header;
    private final String body;
    private final LocalDateTime timestamp;

    /**
     * Constructs a new message with the specified parameters.
     * @param messageHeader The header of the message
     * @param messageBody  The body of the message
     */
    public Message(String messageHeader, String messageBody) {
        this.header = messageHeader;
        this.body = messageBody;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Added method to convert current message to JSON format. This can be used to send a message through sockets, also
     * using Gson and an additional adapter to handle the LocalDateTime type.
     * @return String json representation of a message object.
     */
    public String toJson() {
        class LocalDateTimeTypeAdapter implements JsonSerializer<LocalDateTime> {

            private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.n");

            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type srcType,
                                         JsonSerializationContext context) {

                return new JsonPrimitive(formatter.format(localDateTime));
            }
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();

        return gson.toJson(this);
    }

    /**
     * Added method to create a message from its JSON string. This method has an innerclass LocalDateTimeTypeAdapter,
     * because the GSON package is not able to cast a LocalDateTime from JSON format itself.
     * @param messageString json string representation of a message object.
     * @return Message object from the string.
     */
    public static Message createMessageFromString(String messageString) {
        class LocalDateTimeTypeAdapter implements JsonDeserializer<LocalDateTime> {

            private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.n");

            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT,
                                             JsonDeserializationContext context) throws JsonParseException {

                return LocalDateTime.parse(json.getAsString(), formatter);
            }
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        return gson.fromJson(messageString, Message.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(timestamp, message.timestamp);
    }

    @Override
    public int hashCode() {
        return this.timestamp.hashCode();
    }
}
