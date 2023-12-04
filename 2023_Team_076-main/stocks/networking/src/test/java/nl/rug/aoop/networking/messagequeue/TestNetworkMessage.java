package nl.rug.aoop.networking.messagequeue;

import org.junit.Test;
import nl.rug.aoop.messagequeue.Message;

import static org.junit.Assert.assertEquals;

/**
 * This class contains JUnit tests for the {@link NetworkMessage} class.
 */
public class TestNetworkMessage {

    /**
     * Test the toJson() method of the {@link NetworkMessage} class.
     * This test ensures that the JSON representation of a NetworkMessage object is correctly generated.
     */
    @Test
    public void testToJson() {
        String header = "Test Header";
        String body = "Test Body";
        NetworkMessage networkMessage = new NetworkMessage(header, body);

        String expectedJson = new Message(header, body).toJson();
        String actualJson = networkMessage.toJson();

        expectedJson = removeTimestampFromJson(expectedJson);
        actualJson = removeTimestampFromJson(actualJson);

        assertEquals(expectedJson, actualJson);
    }

    /**
     * Test the createPutMessage() method of the {@link NetworkMessage} class.
     * This test ensures that the createPutMessage() method correctly creates a NetworkMessage with the "MqPut" header.
     */
    @Test
    public void testCreatePutMessage() {
        String header = "Test Header";
        String body = "Test Body";
        Message message = new Message(header, body);
        NetworkMessage networkMessage = NetworkMessage.createPutMessage(message);

        String expectedHeader = "MqPut";
        String expectedBody = message.toJson();

        expectedBody = removeTimestampFromJson(expectedBody);
        String actualBody = removeTimestampFromJson(networkMessage.getBody());

        assertEquals(expectedHeader, networkMessage.getHeader());
        assertEquals(expectedBody, actualBody);
    }

    /**
     * Remove the "timestamp" field from a JSON string.
     *
     * @param json The JSON string from which to remove the "timestamp" field.
     * @return The modified JSON string with the "timestamp" field removed.
     */
    private String removeTimestampFromJson(String json) {
        int startIndex = json.indexOf("\"timestamp\"");
        if (startIndex != -1) {
            int endIndex = json.indexOf(',', startIndex);
            if (endIndex == -1) {
                endIndex = json.indexOf('}', startIndex);
            }
            if (endIndex != -1) {
                json = json.substring(0, startIndex - 1) + json.substring(endIndex);
            }
        }
        return json;
    }
}