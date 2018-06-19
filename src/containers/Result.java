package containers;
import java.util.ArrayList;
import java.util.HashSet;

import interfaces.Keyable;

public class Result implements Keyable{

	private String url = "";
	private String company = "";	
	private String what = "";
	private String where = "";

	private HashSet<String> numbers = new HashSet<String>();
	private HashSet<String> emails = new HashSet<String>();
	private HashSet<Adress> adresses = new HashSet<Adress>();

	public Result(String url){
		this.url = url;
	}

	public static Result parse(String serial) {
		String[] parts = serial.split(";");

		Result result = new Result(parts[0]);

		result.setCompany(parts[1]);
		result.what = parts[2];

		for(int j=3; j<parts.length; j++){
			String part = parts[j];
			if(part.contains("phone")){
				String phones = parts[j];
				String[] phoneParts = phones.split(":");
				for(int k=1; k<phoneParts.length; k++){
					String phone = phoneParts[k];
					result.addNumber(phone);
				}
			}else{
				if(part.contains("emails")){
					String emails = parts[j];
					String[] emailParts = emails.split(":");
					for(int k=1; k<emailParts.length; k++){
						String email = emailParts[k];
						result.addEmail(email);
					}
				}else{
					if(part.contains("adresses")){
						String ads = parts[j];
						String[] adParts = ads.split(":");
						for(int k=1; k<adParts.length; k++){
							String ad = adParts[k];
							String[] subParts = ad.split("#");
							ArrayList<String> tempList = new ArrayList<String>();
							for(String temp : subParts){
								tempList.add(temp);
							}
							for(int l=tempList.size();l<3; l++){
								tempList.add("");
							}
							Adress adr = new Adress(tempList.get(0), tempList.get(1), tempList.get(2));
							result.addAdress(adr);
						}
					}else{
						result.where = parts[j];
					}
				}
			}
		}

		return result;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public void addNumber(String number) {
		this.numbers.add(number.replace(";", ""));
	}

	public void addEmail(String email) {
		this.emails.add(email.replace(";", ""));
	}

	public void addAdress(Adress adress) {
		this.adresses.add(adress);
	}

	public ArrayList<String> getNumbers() {
		ArrayList<String> tempNumbers = new ArrayList<String>();
		tempNumbers.addAll(numbers);
		return tempNumbers;
	}

	public ArrayList<String> getEmails() {
		ArrayList<String> tempEmails = new ArrayList<String>();
		tempEmails.addAll(emails);
		return tempEmails;
	}

	public ArrayList<Adress> getAdresses() {
		ArrayList<Adress> tempAdresses = new ArrayList<Adress>();
		tempAdresses.addAll(adresses);
		return tempAdresses;
	}

	/**
	 * 
	 */
	public String toString(){
		String str = "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + "\n";
		str = str +  "Name: " + "\t" + this.company + "\n";
		str = str + "Url: " + "\t" + this.url + "\n";
		str = str + "What: " + "\t" + this.what + "\n";
		str = str + "Where: " + "\t" + this.where + "\n";
		for(String phone : this.numbers){
			str = str + "Phone no: " + "\t" + phone + "\n";
		}
		for(String email : this.emails){
			str = str + "Email: " + "\t" + email + "\n";
		}
		for(Adress adress : this.adresses){
			str = str + "Adress: " + "\t" + adress.getStreet() + "\t" + adress.getPostal() + "\t" + adress.getCity() + "\n";
		}
		return str;
	}

	public String serialize(){
		String serial = this.url+";"+this.company+";"+this.what+";"+this.where+";";

		if(this.numbers.size()>0){
			serial = serial+"phones:";
		}
		for(String phone : this.numbers){
			serial = serial+phone+":";
		}
		if(this.numbers.size()>0){
			serial = serial.substring(0, serial.length()-1);
			serial = serial + ";";
		}

		if(this.emails.size()>0){
			serial = serial+"emails:";
		}
		for(String email : this.emails){
			serial = serial+email+":";
		}
		if(this.emails.size()>0){
			serial = serial.substring(0, serial.length()-1);
			serial = serial+";";
		}

		if(this.adresses.size()>0){
			serial = serial+"adresses:";
		}
		for(Adress ad : this.adresses){
			serial = serial+ad.serialize()+":";
		}
		if(this.adresses.size()>0){
			serial = serial.substring(0, serial.length()-1);
		}
		return serial;
	}

	@Override
	public String getKey() {
		return this.url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresses == null) ? 0 : adresses.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((emails == null) ? 0 : emails.hashCode());
		result = prime * result + ((numbers == null) ? 0 : numbers.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((what == null) ? 0 : what.hashCode());
		result = prime * result + ((where == null) ? 0 : where.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Result)) {
			return false;
		}
		Result other = (Result) obj;
		if (adresses == null) {
			if (other.adresses != null) {
				return false;
			}
		} else if (!adresses.equals(other.adresses)) {
			return false;
		}
		if (company == null) {
			if (other.company != null) {
				return false;
			}
		} else if (!company.equals(other.company)) {
			return false;
		}
		if (emails == null) {
			if (other.emails != null) {
				return false;
			}
		} else if (!emails.equals(other.emails)) {
			return false;
		}
		if (numbers == null) {
			if (other.numbers != null) {
				return false;
			}
		} else if (!numbers.equals(other.numbers)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		if (what == null) {
			if (other.what != null) {
				return false;
			}
		} else if (!what.equals(other.what)) {
			return false;
		}
		if (where == null) {
			if (other.where != null) {
				return false;
			}
		} else if (!where.equals(other.where)) {
			return false;
		}
		return true;
	}
}
