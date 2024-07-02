import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserInterface {

    private DataManager dataManager;
    private Organization org;
    private Scanner in = new Scanner(System.in);
    private Map<String, String> aggregatedDonationsCache = new HashMap<>();
    private String login;
    private String password;

    public UserInterface(DataManager dataManager, Organization org, String login, String password) {
        this.dataManager = dataManager;
        this.org = org;
        this.login = login;
        this.password = password;
    }

    public void start() {
        while (true) {
            System.out.println("\n\n");
            if (!org.getFunds().isEmpty()) {
                System.out.println("There are " + org.getFunds().size() + " funds in this organization:");
                int count = 1;
                for (Fund f : org.getFunds()) {
                    System.out.println(count + ": " + f.getName());
                    count++;
                }
                System.out.println("Enter the fund number to see more information.");
            }
            System.out.println("Enter 0 to create a new fund");
            System.out.println("Enter 'a' to list all contributions");
            System.out.println("Enter 'c' to change the organization's password");
            System.out.println("Enter 'e' to edit organization information");
            System.out.println("Enter 'l' to log out");
            System.out.println("Enter 'q' or 'quit' to exit");

            while (true) {
                String input = in.next();
                in.nextLine();

                if (input.equals("l")) {
                    System.out.println("Logging out...");
                    return;
                }

                if (input.equals("q") || input.equals("quit")) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }

                try {
                    if (input.equals("a")) {
                        listAllContributions();
                        break;
                    }
                    if (input.equals("c")) {
                        changePassword();
                        break;
                    }
                    if (input.equals("e")) {
                        updateOrganization();
                        break;
                    }
                    int option = Integer.parseInt(input);
                    if (option == 0) {
                        createFund();
                        break;
                    } else {
                        try {
                            displayFund(option);
                            break;
                        } catch (Exception e) {
                            System.out.println("Invalid fund number. Please try again with a valid number.\n");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please try again with a valid option.\n");
                }
            }
        }
    }

    private void changePassword() {
        System.out.println("Please enter the password of the organization:");
        String currentPassword = in.nextLine();
        if (!password.equals(currentPassword.trim())) {
            System.out.println("Wrong password. Returning to the menu.");
            return;
        }
        System.out.println("Please enter the new password:");
        String newPassword1 = in.nextLine();
        while (newPassword1.trim().isEmpty()) {
            System.out.println("Password cannot be blank. Please enter a valid password.");
            newPassword1 = in.nextLine();
        }
        System.out.println("Please re-enter the new password:");
        String newPassword2 = in.nextLine();
        while (newPassword2.trim().isEmpty()) {
            System.out.println("Password cannot be blank. Please enter a valid password.");
            newPassword2 = in.nextLine();
        }
        if (!newPassword1.equals(newPassword2)) {
            System.out.println("Passwords do not match. Returning to the menu.");
            return;
        }
        try {
            boolean success = dataManager.changePassword(login, currentPassword, newPassword1);
            if (success) {
                password = newPassword1;
                System.out.println("Password changed successfully.");
            } else {
                System.out.println("Failed to change password.");
            }
        } catch (Exception e) {
            System.out.println("Error in changing password.");
        }
    }

    private void updateOrganization() {
        System.out.println("Please enter the password of the organization:");
        String typedPassword = in.nextLine();
        if (password.equals(typedPassword.trim())) {
            System.out.println("Correct password!");
            String name = getUpdatedInfo("name", org.getName());
            String description = getUpdatedInfo("description", org.getDescription());
            if (name.equals(org.getName()) && description.equals(org.getDescription())) {
                System.out.println("No changes made to the organization information.");
                return;
            }
            try {
                org = dataManager.updateOrganization(login, password, name, description);
                System.out.println("Organization's information updated successfully.");
            } catch (Exception e) {
                System.out.println("Error in communicating with RESTful API.");
            }
        } else {
            System.out.println("Wrong password!");
        }
    }

    private String getUpdatedInfo(String type, String tmp) {
        System.out.println("Do you want to change the " + type +" of the organization? (yes/no)");
        String input = in.nextLine();
        while (true) {
            if (input.equalsIgnoreCase("yes")) {
                System.out.print("Enter the new " + type + ": ");
                tmp = in.nextLine();
                if (tmp.trim().isEmpty()) {
                    System.out.println(type + " cannot be blank. Please enter a valid " + type + ".");
                    continue;
                }
                break;
            } else if (input.equalsIgnoreCase("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                input = in.nextLine();
            }
        }
        return tmp;
    }

    private void listAllContributions() {
        List<Donation> allDonations = new ArrayList<>();
        for (Fund fund : org.getFunds()) {
            allDonations.addAll(fund.getDonations());
        }
        allDonations.sort((d1, d2) -> {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date date1 = parser.parse(d1.getDate());
                Date date2 = parser.parse(d2.getDate());
                return date2.compareTo(date1);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        System.out.println("All contributions to the organization's funds:");
        for (Donation donation : allDonations) {
            try {
                Date date = parser.parse(donation.getDate());
                String formattedDate = formatter.format(date);
                String fundName = Fund.getNameById(org.getFunds(), donation.getFundId());
                System.out.println("Fund: " + fundName + ", Amount: $" + donation.getAmount() + ", Date: " + formattedDate);
            } catch (ParseException e) {
                System.out.println("Date parsing error");
            }
        }
    }

    public void createFund() {
        String name = getFundInfo("name");
        String description = getFundInfo("description");
        double target = getFundTarget();
        while (true) {
            try {
                Fund fund = dataManager.createFund(org.getId(), name, description, target);
                org.getFunds().add(fund);
                break;
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Would you like to retry? (yes/no)");
                String input = in.nextLine();
                if (!input.equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
    }

    private String getFundInfo(String type) {
        while (true) {
            System.out.print("Enter the fund " + type + ": ");
            String input = in.nextLine();
            if (!input.trim().isEmpty()) {
                return input;
            }
            System.out.println("Blank " + type + ". Please enter a valid " + type + ".\n");
        }
    }

    private double getFundTarget() {
        while (true) {
            System.out.print("Enter the fund target: ");
            String input = in.nextLine();
            try {
                double target = Double.parseDouble(input);
                if (target >= 0) {
                    return target;
                } else {
                    System.out.println("Please enter a non-negative target amount.\n");
                }
            } catch (Exception e) {
                System.out.println("Invalid target amount. Please enter a valid target amount.\n");
            }
        }
    }

    private String getContributorID() {
        while (true) {
            System.out.print("Enter the contributor ID: ");
            String input = in.nextLine();
            if (!input.trim().isEmpty()) {
                return input;
            }
            System.out.println("Blank contributor ID. Please enter a valid contributor ID.\n");
        }
    }

    private double getDonationAmount() {
        while (true) {
            System.out.print("Enter the donation amount: ");
            String input = in.nextLine();
            try {
                double amount = Double.parseDouble(input);
                if (amount >= 0) {
                    amount = Math.round(amount * 100) / 100.0;
                    return amount;
                } else {
                    System.out.println("Please enter a non-negative donation amount.\n");
                }
            } catch (Exception e) {
                System.out.println("Invalid donation amount. Please enter a valid donation amount.\n");
            }
        }
    }

    private void listDonation(Fund fund) {
        double totalDonations = 0;
        List<Donation> donations = fund.getDonations();
        System.out.println("Number of donations: " + donations.size());

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        int size = donations.size();
        for (int i = size -1; i >= 0; i--) {
            Donation donation = donations.get(i);
            totalDonations += donation.getAmount();
            try {
                Date date = parser.parse(donation.getDate());
                String formattedDate = formatter.format(date);
                System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + formattedDate);
            } catch (ParseException e) {
                System.out.println("Date parsing error");
            }
        }
        System.out.println("Total donations: $" + totalDonations + " (" + Math.round(totalDonations / fund.getTarget() * 10000) / 100.0 + "% of target)");
    }

    public void displayFund(int fundNumber) {
        Fund fund = org.getFunds().get(fundNumber - 1);
        System.out.println("\n\n");
        System.out.println("Here is information about this fund:");
        System.out.println("Name: " + fund.getName());
        System.out.println("Description: " + fund.getDescription());
        System.out.println("Target: $" + fund.getTarget());

        while (true) {
            System.out.println("Enter 'a' for aggregated result, 'd' to make a donation, 'l' for list individual donations, 'x' to delete this fund.");
            String input = in.nextLine().trim().toLowerCase();
            if (input.equals("a")) {
                String cacheKey = String.valueOf(fund.getId());
                String aggregatedDonations = aggregatedDonationsCache.get(cacheKey);
                if (aggregatedDonations == null) {
                    aggregatedDonations = aggregateDonations(fund);
                    aggregatedDonationsCache.put(cacheKey, aggregatedDonations);
                }
                System.out.println("Aggregate donations by contributor:");
                System.out.println(aggregatedDonations);
                break;
            } else if (input.equals("d")) {
                while (true) {
                    String contributorId = getContributorID();
                    double amount = getDonationAmount();
                    try {
                        fund = dataManager.makeDonation(contributorId, amount, fund);
                        aggregatedDonationsCache.remove(String.valueOf(fund.getId()));
                        listDonation(fund);
                        break;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Please try again!\n");
                    }
                }
                break;
            } else if (input.equals("l")) {
                listDonation(fund);
                break;
            } else if (input.equals("x")) {
                System.out.println("Are you sure you want to delete this fund? (yes/no)");
                String confirm = in.nextLine().trim().toLowerCase();
                if (confirm.equals("yes")) {
                    try {
                        dataManager.deleteFund(fund.getId());
                        org.getFunds().remove(fund);
                        System.out.println(fund.getName() + " has been deleted.");
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
            } else {
                System.out.println("Invalid input. Please enter 'a','d', 'l' or 'x'.");
            }
        }
        System.out.println("Press the Enter key to go back to the listing of funds");
        in.nextLine();
    }

    private String aggregateDonations(Fund fund) {
        Map<String, Double> contributorTotalMap = new HashMap<>();
        Map<String, Integer> contributorCountMap = new HashMap<>();
        for (Donation donation : fund.getDonations()) {
            String contributor = donation.getContributorName();
            double amount = donation.getAmount();
            contributorTotalMap.put(contributor, contributorTotalMap.getOrDefault(contributor, 0.0) + amount);
            contributorCountMap.put(contributor, contributorCountMap.getOrDefault(contributor, 0) + 1);
        }
        StringBuilder result = new StringBuilder();
        List<Map.Entry<String, Double>> sortedContributors = new ArrayList<>(contributorTotalMap.entrySet());
        sortedContributors.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        for (Map.Entry<String, Double> entry : sortedContributors) {
            String contributor = entry.getKey();
            double totalAmount = entry.getValue();
            int donationCount = contributorCountMap.get(contributor);
            result.append(contributor)
                    .append(", ")
                    .append(donationCount)
                    .append(" donation(s), $")
                    .append(totalAmount)
                    .append(" total\n");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        DataManager ds = new DataManager(new WebClient("localhost", 3001));
        String login = "";
        String password = "";
        if (args.length == 2) {
            login = args[0];
            password = args[1];
        }
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the Fundraising System!");
        System.out.println("Please Wait, Connecting to Server... ");
        while (true) {
            String[] userInfo = runProgram(ds, login, password);
            login = userInfo[0];
            password = userInfo[1];
            System.out.println("Enter 'b' to log back, 's' to switch user, or 'q' to quit application.");
            String input = in.nextLine();
            while (true) {
                if (input.equals("b")) {
                    System.out.println("Logging back to " + login);
                    break;
                } else if (input.equals("s")) {
                    System.out.print("Enter login ID: ");
                    login = in.nextLine();
                    System.out.print("Enter password: ");
                    password = in.nextLine();
                    System.out.println("Switching to Organization: " + login);
                    break;
                } else if (input.equals("q")) {
                    System.out.println("Goodbye!");
                    return;
                } else {
                    System.out.println("Invalid input. Please enter 'b', 's', or 'q'.");
                    input = in.nextLine();
                }
            }
        }
    }

    private static String[] runProgram(DataManager ds, String login, String password) {
        Scanner in = new Scanner(System.in);
        if (login.isEmpty() && password.isEmpty()) {
            while (true) {
                System.out.println("1. Login");
                System.out.println("2. Create new organization");
                System.out.print("Choose an option: ");
                try {
                    int option = Integer.parseInt(in.nextLine());
                    if (option == 1) {
                        login = getOrgInfo("login");
                        password = getOrgInfo("password");
                        Organization org = ds.attemptLogin(login, password);
                        if (org == null) {
                            System.out.println("Login failed.");
                            System.out.println("Please provide correct login and password.\n");
                        } else {
                            UserInterface ui = new UserInterface(ds, org, login, password);
                            ui.start();
                            return new String[]{login, password};
                        }
                    } else if (option == 2) {
                        return createOrganization(ds);
                    } else {
                        System.out.println("Invalid option.");
                        System.out.println("Please choose a valid option.\n");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid option.");
                    System.out.println("Please choose a valid option.\n");
                }
            }
        } else {
            boolean failedLogin = false;
            while (true) {
                if (failedLogin) {
                    login = getOrgInfo("login");
                    password = getOrgInfo("password");
                }
                try {
                    Organization org = ds.attemptLogin(login, password);
                    if (org == null) {
                        System.out.println("Login failed.");
                        System.out.println("Please provide correct login and password.\n");
                        failedLogin = true;
                    } else {
                        UserInterface ui = new UserInterface(ds, org, login, password);
                        ui.start();
                        return new String[]{login, password};
                    }
                } catch (IllegalStateException e) {
                    System.out.println("Login failed");
                    System.out.println("Please try again.\n");
                }
            }
        }
    }

    private static String getOrgInfo(String type) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("Enter the organization " + type + ": ");
            String input = in.nextLine();
            if (!input.trim().isEmpty()) {
                return input;
            }
            System.out.println("Blank " + type + ". Please enter a valid " + type + ".\n");
        }
    }

    private static String[] createOrganization(DataManager ds) {
        while (true) {
            String login = getOrgInfo("login");
            String password = getOrgInfo("password");
            String name = getOrgInfo("name");
            String description = getOrgInfo("description");

            try {
                Organization newOrg = ds.createOrganization(login, password, name, description);
                if (newOrg == null) {
                    System.out.println("Failed to create organization.");
                    System.out.println("Please try again.\n");
                } else {
                    System.out.println("New organization created: " + newOrg.getName());
                    UserInterface ui = new UserInterface(ds, newOrg, login, password);
                    ui.start();
                    return new String[]{login, password};
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again.\n");
            }
        }
    }
}
