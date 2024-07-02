import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
public class DataManager_changePassword_Test {
    @Test(expected = IllegalArgumentException.class)
    public void testNullLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.changePassword(null, "12345", "54321");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCurrentPassword() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.changePassword("admin", null, "54321");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNewPassword() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.changePassword("admin", "12345", null);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        assertFalse(dm.changePassword("admin", "12345", "54321"));
    }

    @Test
    public void testSuccessfulPasswordChange() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
            }
        });
        assertTrue(dm.changePassword("admin", "12345", "54321"));
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorPasswordChange() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        dm.changePassword("admin", "12345", "54321");
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionHandling() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Network error");
            }
        });
        dm.changePassword("admin", "12345", "54321");
    }

}
