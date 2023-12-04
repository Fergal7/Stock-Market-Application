package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class TestMessage {

    private Message message;
    private String messageHeader;
    private String messageBody;


    @BeforeEach
    void setUp() {
        messageHeader = "header";
        messageBody = "body";
        message = new Message(messageHeader, messageBody);
    }


    @Test
    void testMessageConstructor() {
        assertEquals(messageHeader, message.getHeader());
        assertEquals(messageBody, message.getBody());
        assertNotNull(message.getTimestamp());
    }

    @Test
    void testMessageImmutable() {
        List<Field> fields = List.of(Message.class.getDeclaredFields());
        fields.forEach(field -> {
            assertTrue(Modifier.isFinal(field.getModifiers()), field.getName() + " is not final");
        });
    }

    @Test
    void testTimestampWithinReasonableRange() {
        Message message = new Message("header", "body");

        LocalDateTime currentTime = LocalDateTime.now();

        Duration tolerance = Duration.ofSeconds(1);

        assertTrue(currentTime.minus(tolerance).isBefore(message.getTimestamp()));
        assertTrue(currentTime.plus(tolerance).isAfter(message.getTimestamp()));
    }

    @Test
    public void testSerializationDeserialization() {
        Message originalMessage = new Message("header", "body");
        String jsonString = originalMessage.toJson();
        Message deserializedMessage = Message.createMessageFromString(jsonString);

        // You might need to override equals method in Message class to make this work
        assertEquals(originalMessage.getBody(), deserializedMessage.getBody());
        assertEquals(originalMessage.getHeader(), deserializedMessage.getHeader());
    }
}
