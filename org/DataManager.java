import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataManager {

    private final WebClient client;

    public Map<String, String> ContributorNameCache = new HashMap<>();

    public DataManager(WebClient client) {
        this.client = client;
    }

    /**
     * Attempt to log the user into an Organization account using the login and password.
     * This method uses the /findOrgByLoginAndPassword endpoint in the API
     *
     * @return an Organization object if successful; null if unsuccessful
     */
    public Organization attemptLogin(String login, String password) {
        if (login == null || password == null) {
            throw new IllegalArgumentException("Login and password cannot be null");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("login", login);
            map.put("password", password);
            String response = client.makeRequest("/findOrgByLoginAndPassword", map);

            if (response == null) {
                throw new IllegalStateException("WebClient returned null response");
            }

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");

            if (status.equals("success")) {
                JSONObject data = (JSONObject) json.get("data");
                String fundId = (String) data.get("_id");
                String name = (String) data.get("name");
                String description = (String) data.get("description");
                Organization org = new Organization(fundId, name, description);

                JSONArray funds = (JSONArray) data.get("funds");
                Iterator it = funds.iterator();
                while (it.hasNext()) {
                    JSONObject fund = (JSONObject) it.next();
                    fundId = (String) fund.get("_id");
                    name = (String) fund.get("name");
                    description = (String) fund.get("description");
                    Number targetNumber = (Number) fund.get("target");
                    double target = targetNumber.doubleValue();

                    Fund newFund = new Fund(fundId, name, description, target);

                    JSONArray donations = (JSONArray) fund.get("donations");
                    List<Donation> donationList = new LinkedList<>();
                    Iterator it2 = donations.iterator();
                    while (it2.hasNext()) {
                        JSONObject donation = (JSONObject) it2.next();
                        String contributorId = (String) donation.get("contributor");
                        String contributorName = getContributorName(contributorId);
                        Number amountNumber = (Number) donation.get("amount");
                        double amount = amountNumber.doubleValue();
                        String date = (String) donation.get("date");
                        donationList.add(new Donation(fundId, contributorName, amount, date));
                    }
                    newFund.setDonations(donationList);
                    org.addFund(newFund);
                }
                return org;
            } else if (status.equals("error")) {
                throw new IllegalStateException("Error from WebClient");
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error in communicating with server");
        }
    }

    /**
     * Look up the name of the contributor with the specified ID.
     * This method uses the /findContributorNameById endpoint in the API.
     *
     * @return the name of the contributor on success; null if no contributor is found
     */
    public String getContributorName(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Contributor ID cannot be null");
        }
        if (ContributorNameCache.containsKey(id)) {
            return ContributorNameCache.get(id);
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            String response = client.makeRequest("/findContributorNameById", map);

            if (response == null) {
                throw new IllegalStateException("WebClient returned null response");
            }

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");

            if (status.equals("success")) {
                String name = (String) json.get("data");
                ContributorNameCache.put(id, name);
                return name;
            } else if (status.equals("error")) {
                throw new IllegalStateException("Error from WebClient");
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error in communicating with server");
        }
    }

    /**
     * This method creates a new fund in the database using the /createFund endpoint in the API
     *
     * @return a new Fund object if successful; null if unsuccessful
     */
    public Fund createFund(String orgId, String name, String description, double target) {
        if (orgId == null || name == null || description == null) {
            throw new IllegalArgumentException("orgId, name, and description cannot be null");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("orgId", orgId);
            map.put("name", name);
            map.put("description", description);
            map.put("target", target);
            String response = client.makeRequest("/createFund", map);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");

            if (status.equals("success")) {
                JSONObject fund = (JSONObject) json.get("data");
                String fundId = (String) fund.get("_id");
                return new Fund(fundId, name, description, target);
            } else {
                throw new IllegalStateException("Error from WebClient");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error in communicating with server");
        }
    }

    public boolean deleteFund(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Fund ID cannot be null or empty");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            String response = client.makeRequest("/deleteFund", map);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");

            if (status.equals("success")) {
                return true;
            } else {
                throw new IllegalStateException("Error from WebClient");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error in communicating with server");
        }
    }

    public Fund makeDonation(String contributorId, double amount, Fund fund) {
        if (contributorId == null) {
            throw new IllegalArgumentException("Contributor ID cannot be null");
        } else if (fund == null) {
            throw new IllegalArgumentException("Fund cannot be null");
        } else if (amount < 0) {
            throw new IllegalArgumentException("Donation amount must be greater than 0");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("contributor", contributorId);
            map.put("amount", amount);
            String fundId = fund.getId();
            map.put("fund", fundId);
            String response = client.makeRequest("/makeDonation", map);
            if (response == null) {
                throw new IllegalStateException("WebClient returned null response");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (status.equals("success")) {
                JSONObject obj = (JSONObject) json.get("data");
                String date = (String) obj.get("date");
                List<Donation> donations = fund.getDonations();
                Donation donation = new Donation(fundId, getContributorName(contributorId), amount, date);
                donations.add(donation);
                fund.setDonations(donations);
                return fund;
            } else {
                throw new IllegalStateException("Contributor ID not exist in database");
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (Exception e) {
            throw new IllegalStateException("Error in communicating with server");
        }
    }

    public Organization createOrganization(String login, String password, String name, String description) {
        if (login == null || password == null || name == null || description == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("login", login);
            map.put("password", password);
            map.put("name", name);
            map.put("description", description);
            String response = client.makeRequest("/createOrg", map);
            if (response == null) {
                throw new IllegalStateException("WebClient returned null response");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (status.equals("success")) {
                JSONObject data = (JSONObject) json.get("data");
                String orgId = (String) data.get("_id");
                String orgName = (String) data.get("name");
                String orgDescription = (String) data.get("description");
                return new Organization(orgId, orgName, orgDescription);
            } else if (status.equals("duplicate")) {
                throw new IllegalStateException("Login name already exists");
            } else {
                throw new IllegalStateException("Error from WebClient");
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public boolean changePassword(String login, String currentPassword, String newPassword) {
        if (login == null || currentPassword == null || newPassword == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("login", login);
            map.put("currentPassword", currentPassword);
            map.put("newPassword", newPassword);
            String response = client.makeRequest("/changePassword", map);
            if (response == null) {
                throw new IllegalStateException("WebClient returned null response");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (status.equals("success")) {
                return true;
            } else {
                throw new IllegalStateException("Error from WebClient");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error in communicating with server");
        }
    }

    public Organization updateOrganization(String login, String password, String name, String description) {
        if (login == null || password == null || name == null || description == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("login", login);
            map.put("password", password);
            map.put("name", name);
            map.put("description", description);
            String response = client.makeRequest("/updateOrg", map);
            if (response == null) {
                throw new IllegalStateException("WebClient returned null response");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (status.equals("success")) {
                JSONObject data = (JSONObject) json.get("data");
                String orgId = (String) data.get("_id");
                String orgName = (String) data.get("name");
                String orgDescription = (String) data.get("description");
                return new Organization(orgId, orgName, orgDescription);
            } else  {
                throw new IllegalStateException("Error from WebClient");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error in communicating with server");
        }
    }
}
