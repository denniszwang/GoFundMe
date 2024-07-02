import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_createOrganization_Test {

    @Test
    public void testSuccessfulCreation() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"org1\",\"name\":\"Org Name\",\"description\":\"Org Description\"}}";
            }
        });
        Organization org = dm.createOrganization("login", "password", "Org Name", "Org Description");
        assertNotNull(org);
        assertEquals("org1", org.getId());
        assertEquals("Org Name", org.getName());
        assertEquals("Org Description", org.getDescription());
    }

    @Test(expected = IllegalStateException.class)
    public void testFailureStatus() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\"}";
            }
        });
        assertNull(dm.createOrganization("login", "password", "Org Name", "Org Description"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicate() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"duplicate\"}";
            }
        });
        assertNull(dm.createOrganization("login", "password", "Org Name", "Org Description"));
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionHandling() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Network error");
            }
        });
        assertNull(dm.createOrganization("login", "password", "Org Name", "Org Description"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        assertNull(dm.createOrganization(null, "password", "Org Name", "Org Description"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPassword() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        assertNull(dm.createOrganization("login", null, "Org Name", "Org Description"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        assertNull(dm.createOrganization("login", "password", null, "Org Description"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDescription() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        assertNull(dm.createOrganization("login", "password", "Org Name", null));
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        assertNull(dm.createOrganization("login", "password", "Org Name", "Org Description"));
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorStatus() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        assertNull(dm.createOrganization("login", "password", "Org Name", "Org Description"));
    }
}
