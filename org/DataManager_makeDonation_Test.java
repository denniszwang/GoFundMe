import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_makeDonation_Test {

    @Test
    public void testSuccessfulDonation() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource.equals("/makeDonation")) {
                    return "{\"status\":\"success\",\"data\":{\"date\":\"2024-06-27\"}}";
                } else if (resource.equals("/findContributorNameById")) {
                    return "{\"status\":\"success\",\"data\":\"Contributor Name\"}";
                }
                return null;
            }
        });
        String contributorId = "contributor1";
        double amount = 50;
        Fund fund = new Fund("fund1", "Fund Name", "Fund Description", 10000);
        fund = dm.makeDonation(contributorId, amount, fund);
        assertNotNull(fund);
        assertEquals(1, fund.getDonations().size());
        Donation donation = fund.getDonations().get(0);
        assertEquals("Contributor Name", donation.getContributorName());
        assertEquals(amount, donation.getAmount(), 0.01);
        assertEquals("2024-06-27", donation.getDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullContributorId() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        Fund fund = new Fund("fund1", "Fund Name", "Fund Description", 10000);
        dm.makeDonation(null, 50, fund);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAmount() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        Fund fund = new Fund("fund1", "Fund Name", "Fund Description", 10000);
        dm.makeDonation("contributor1", -1, fund);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFund() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.makeDonation("contributor1", 50, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        Fund fund = new Fund("fund1", "Fund Name", "Fund Description", 10000);
        dm.makeDonation("contributor1", 50, fund);
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorStatus() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        Fund fund = new Fund("fund1", "Fund Name", "Fund Description", 10000);
        dm.makeDonation("contributor1", 50, fund);
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionHandling() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Network error");
            }
        });
        Fund fund = new Fund("fund1", "Fund Name", "Fund Description", 10000);
        dm.makeDonation("contributor1", 50, fund);
    }
}
