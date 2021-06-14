package fusion;

import java.util.Collection;

public class Results {
	
	private Collection<Collection<String>> aggregation_result;
	
	public Results(Collection<Collection<String>> res) {
		this.aggregation_result = res;
	}
	
	public String printExtensionsEnumeration() {
		return aggregation_result.toString();
	}
	
	public boolean credulousAcceptance(String arg) {
		for(Collection<String> extension : aggregation_result) {
			if(extension.contains(arg))
				return true;
		}
		return false;
	}
	
	public String printCredulousAcceptance(String arg) {
		if(credulousAcceptance(arg))
			return "YES";
		else
			return "NO";
	}
	
	public boolean skepticalAcceptance(String arg) {
		for(Collection<String> extension : aggregation_result) {
			if(!extension.contains(arg))
				return false;
		}
		return true;
	}
	
	public String printSkepticalAcceptance(String arg) {
		if(skepticalAcceptance(arg))
			return "YES";
		else
			return "NO";
	}
	
	public String printAcceptance(String option, String arg) {
		String res = new String();
		switch(option) {
			case "DC":
				res = printCredulousAcceptance(arg);
				break;
			case "DS":
				res = printSkepticalAcceptance(arg);
				break;
			default:
				System.err.println("Unknown option : " + option);
				System.exit(1);
		}
		return res;
	}

}
