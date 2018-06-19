package containers;

public class Query {

	private String where = "";
	private String what = "";
	
	public Query(String what, String where){
		this.what = what;
		this.where = where;
	}
	
	public Query(String what){
		this.what = what;
	}
	
	public void setWhere(String where){
		this.where = where;
	}
	
	public String getWhat(){
		return this.what;
	}
	
	public String getWhere(){
		return this.where;
	}
	
	public String toString(){
		return (this.what + ", " + this.where).trim();
	}
}
