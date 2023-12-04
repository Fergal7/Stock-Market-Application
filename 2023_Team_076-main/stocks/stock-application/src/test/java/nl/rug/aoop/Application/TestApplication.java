package nl.rug.aoop.Application;

import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.MessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestApplication {

    private StockExchange mockStockExchange;
    private SimpleViewFactory mockViewFactory;
    private MessageQueue mockMessageQueue;


    @BeforeEach
    public void setUp() {
        mockStockExchange = mock(StockExchange.class);
        mockViewFactory = mock(SimpleViewFactory.class);
        mockMessageQueue = mock(MessageQueue.class);
    }

    @Test
    public void testApplicationInitialization() throws IOException {
        when(mockMessageQueue.getSize()).thenReturn(0);

        new Application(mockStockExchange, mockViewFactory);

        verify(mockViewFactory).createView(mockStockExchange);
    }

}
