import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_createFund_Test {

	@Test
	public void testSuccessfulCreation() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";

			}
		});
		Fund f = dm.createFund("12345", "new fund", "this is the new fund", 10000);
		assertNotNull(f);
		assertEquals("this is the new fund", f.getDescription());
		assertEquals("12345", f.getId());
		assertEquals("new fund", f.getName());
		assertEquals(10000, f.getTarget(), 0.0001);
	}

	@Test(expected = IllegalStateException.class)
	public void testUnsuccessfulCreation() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"failure\"}";
			}
		});
		dm.createFund("12345", "new fund", "this is the new fund", 10000);
	}

	@Test(expected = IllegalStateException.class)
	public void testExceptionHandling() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				throw new RuntimeException("Network error");
			}
		});
		dm.createFund("12345", "new fund", "this is the new fund", 10000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullOrgId() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001));
		dm.createFund(null, "new fund", "this is the new fund", 10000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001));
		dm.createFund("12345", null, "this is the new fund", 10000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullDescription() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001));
		dm.createFund("12345", "new fund", null, 10000);
	}
}
