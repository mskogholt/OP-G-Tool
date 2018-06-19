package containers;

public class Adress {

	private String street = "";
	private String postal = "";
	private String city = "";
	
	public Adress(){
		
	}
	
	public Adress(String street, String postal, String city){
		this.street = street.replace(";", "");
		this.postal = postal.replace(";", "");
		this.city = city.replace(";", "");
	}
	
	public Adress(String street, String postal){
		this.street = street.replace(";", "");
		this.postal = postal.replace(";", "");
	}
	
	public Adress(String street){
		this.street = street.replace(";", "");
	}
	
	public void setStreet(String street){
		this.street = street.replace(";", "");
	}
	
	public String getStreet(){
		return this.street;
	}
	
	public void setPostal(String postal){
		this.postal = postal.replace(";", "");
	}
	
	public String getPostal(){
		return this.postal;
	}
	
	public void setCity(String city){
		this.city = city.replace(";", "");
	}
	
	public String getCity(){
		return this.city;
	}
	
	public String serialize(){
		String serial = this.street+"#"+this.postal+"#"+this.city;
		return serial;
	}
	
	public int hashCode(){
		String toHash = this.street+this.postal+this.city;
		return toHash.hashCode();
	}
	
	public boolean equals(Object o){
		if( o instanceof Adress ){
			if( ((Adress) o).getStreet().equals(this.getStreet()) || ((Adress) o).getPostal().equals(this.getPostal())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
