public class Donation {
	
	private String fundId;
	private String contributorName;
	private double amount;
	private String date;
	
	public Donation(String fundId, String contributorName, double amount, String date) {
		this.fundId = fundId;
		this.contributorName = contributorName;
		this.amount = amount;
		this.date = date;
	}

	public String getFundId() {
		return fundId;
	}

	public String getContributorName() {
		return contributorName;
	}

	public double getAmount() {
		return amount;
	}

	public String getDate() {
		return date;
	}
	
	

}
