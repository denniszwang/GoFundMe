import java.util.LinkedList;
import java.util.List;

public class Fund {

	private String id;
	private String name;
	private String description;
	private double target;
	private List<Donation> donations;
	
	public Fund(String id, String name, String description, double target) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.target = target;
		donations = new LinkedList<>();
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public double getTarget() {
		return target;
	}

	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}
	
	public List<Donation> getDonations() {
		return donations;
	}

	public static String getNameById(List<Fund> funds, String id) {
		for (Fund fund : funds) {
			if (fund.getId().equals(id)) {
				return fund.getName();
			}
		}
		return null;
	}
}
