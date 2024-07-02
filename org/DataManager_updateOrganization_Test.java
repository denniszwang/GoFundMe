import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_updateOrganization_Test {
    @Test
    public void testUpdateOrganization() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"123456\",\"login\":\"admin\",\"password\":\"12345\",\"name\":\"admin\",\"description\":\"testing\", \"fund\":\"[]\"}}";
            }
        });
        Organization org = dm.updateOrganization("admin", "12345", "admin", "testing");
        assertNotNull(org);
        assertEquals("123456", org.getId());
        assertEquals("admin", org.getName());
        assertEquals("testing", org.getDescription());
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorStatus() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        assertNull(dm.updateOrganization("admin", "12345", "admin", "testing"));
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionHandling() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Network error");
            }
        });
        dm.updateOrganization("admin", "12345", "admin", "testing");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"123456\",\"login\":\"admin\",\"password\":\"12345\",\"name\":\"admin\",\"description\":\"testing\", \"fund\":\"[]\"}}";
            }
        });
        assertNull(dm.updateOrganization(null, "12345", "admin", "testing"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPassword() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"123456\",\"login\":\"admin\",\"password\":\"12345\",\"name\":\"admin\",\"description\":\"testing\", \"fund\":\"[]\"}}";
            }
        });
        assertNull(dm.updateOrganization("admin", null, "admin", "testing"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"123456\",\"login\":\"admin\",\"password\":\"12345\",\"name\":\"admin\",\"description\":\"testing\", \"fund\":\"[]\"}}";
            }
        });
        assertNull(dm.updateOrganization("admin", "12345", null, "testing"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDescription() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"123456\",\"login\":\"admin\",\"password\":\"12345\",\"name\":\"admin\",\"description\":\"testing\", \"fund\":\"[]\"}}";
            }
        });
        assertNull(dm.updateOrganization("admin", "12345", "admin", null));
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        assertNull(dm.updateOrganization("admin", "12345", "admin", "testing"));
    }
}
