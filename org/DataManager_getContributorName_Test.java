import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_getContributorName_Test {

    @Test
    public void testSuccessfulContributorNameRetrieval() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"John Doe\"}";
            }
        });
        String name = dm.getContributorName("1");
        assertEquals("John Doe", name);
        name = dm.getContributorName("1");
        assertEquals("John Doe", name);
    }

    @Test
    public void testUnsuccessfulContributorNameRetrieval() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\"}";
            }
        });
        String name = dm.getContributorName("1");
        assertNull(name);
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionHandling() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Network error");
            }
        });
        dm.getContributorName("1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullId() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.getContributorName(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.getContributorName("1");
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorStatus() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        dm.getContributorName("1");
    }
}
