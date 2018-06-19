/**
 * 
 */
package models;

/**
 * @author martin.skogholt
 *
 */
public class TerminationLimit {
	
	private String type;
	private int limit;
	
	/**
	 * @param type "Page Limit" or "Time Limit"
	 * @param limit
	 */
	public TerminationLimit(String type, int limit) {
		this.type = type;
		this.limit = limit;
	}
	
	public int getLimit() {
		return this.limit;
	}
	
	/**
	 * 
	 * @return type "Page Limit" or "Time Limit"
	 */
	public String getType() {
		return this.type;
	}
	
	public boolean terminate(int reached) {
		return reached>limit;
	}
}
