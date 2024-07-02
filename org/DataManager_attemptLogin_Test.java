import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_attemptLogin_Test {
    @Test
    public void testSuccessfulLoginWithMultipleFundsAndDonations() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource.contains("Login")) {
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"org1\",\"name\":\"Org Name\",\"description\":\"Org Description\",\"funds\":[" +
                            "{\"_id\":\"fund1\",\"name\":\"Fund Name 1\",\"description\":\"Fund Description 1\",\"target\":10000,\"donations\":[" +
                            "{\"contributor\":\"1\",\"amount\":900,\"date\":\"2024-06-12\"}," +
                            "{\"contributor\":\"2\",\"amount\":1000,\"date\":\"2024-06-13\"}" +
                            "]}," +
                            "{\"_id\":\"fund2\",\"name\":\"Fund Name 2\",\"description\":\"Fund Description 2\",\"target\":20000,\"donations\":[" +
                            "{\"contributor\":\"3\",\"amount\":5000,\"date\":\"2024-06-14\"}" +
                            "]}" + "{\"_id\":\"fund3\",\"name\":\"Fund Name 3\",\"description\":\"Fund Description 3\",\"target\":30000,\"donations\":[]}" +
                            "]}}";
                }
                return "{\"status\":\"success\",\"data\":\"John Doe\"}";
            }
        });
        Organization org = dm.attemptLogin("login", "password");
        assertNotNull(org);
        assertEquals("org1", org.getId());
        assertEquals("Org Name", org.getName());
        assertEquals("Org Description", org.getDescription());
        assertEquals(3, org.getFunds().size());
        Fund fund1 = org.getFunds().get(0);
        assertEquals("fund1", fund1.getId());
        assertEquals("Fund Name 1", fund1.getName());
        assertEquals("Fund Description 1", fund1.getDescription());
        assertEquals(10000, fund1.getTarget(), 0.01);
        assertEquals(2, fund1.getDonations().size());
        assertEquals(900, fund1.getDonations().get(0).getAmount(), 0.01);
        assertEquals(1000, fund1.getDonations().get(1).getAmount(), 0.01);
        Fund fund2 = org.getFunds().get(1);
        assertEquals(1, fund2.getDonations().size());
        Fund fund3 = org.getFunds().get(2);
        assertEquals(0, fund3.getDonations().size());
    }

    @Test
    public void testUnsuccessfulLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\"}";
            }
        });
        Organization org = dm.attemptLogin("login", "password");
        assertNull(org);
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionHandling() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Network error");
            }
        });
        Organization org = dm.attemptLogin("login", "password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.attemptLogin(null, "password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPassword() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.attemptLogin("login", null);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.attemptLogin("login", "password");
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorStatus() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        dm.attemptLogin("login", "password");
    }
}
