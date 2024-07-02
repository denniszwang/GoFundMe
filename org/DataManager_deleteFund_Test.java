import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_deleteFund_Test {
    @Test
    public void testSuccessfulDeletion() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
            }
        });
        assertTrue(dm.deleteFund("12345"));
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsuccessfulDeletion() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\"}";
            }
        });
        assertFalse(dm.deleteFund("12345"));
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionHandling() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Network error");
            }
        });
        assertFalse(dm.deleteFund("12345"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.deleteFund(null);
    }
}
