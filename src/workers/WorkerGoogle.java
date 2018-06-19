/**
 * 
 */
package workers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import containers.Adress;
import containers.Result;
import containers.concurrency.RandomGenerator;
import graphicalinterfaces.MessagePanel;
import models.TerminationLimit;
import models.WebBrowser;

/**
 * @author martin.skogholt
 *
 */
public class WorkerGoogle extends Worker{

	private static final String regex = "([+]{1}|00){1}(999|998|997|996|995|994|993|992|991|990|979|978|977|976|975|974|973|972|971|970|"
			+ "969|968|967|966|965|964|963|962|961|960|899|898|897|896|895|894|893|892|891|890|889|888|"
			+ "887|886|885|884|883|882|881|880|879|878|877|876|875|874|873|872|871|870|859|858|857|856|"
			+ "855|854|853|852|851|850|839|838|837|836|835|834|833|832|831|830|809|808|807|806|805|804|"
			+ "803|802|801|800|699|698|697|696|695|694|693|692|691|690|689|688|687|686|685|684|683|682|"
			+ "681|680|679|678|677|676|675|674|673|672|671|670|599|598|597|596|595|594|593|592|591|590|"
			+ "509|508|507|506|505|504|503|502|501|500|429|428|427|426|425|424|423|422|421|420|389|388|"
			+ "387|386|385|384|383|382|381|380|379|378|377|376|375|374|373|372|371|370|359|358|357|356|"
			+ "355|354|353|352|351|350|299|298|297|296|295|294|293|292|291|290|289|288|287|286|285|284|"
			+ "283|282|281|280|269|268|267|266|265|264|263|262|261|260|259|258|257|256|255|254|253|252|"
			+ "251|250|249|248|247|246|245|244|243|242|241|240|239|238|237|236|235|234|233|232|231|230|"
			+ "229|228|227|226|225|224|223|222|221|220|219|218|217|216|215|214|213|212|211|210|98|95|94|"
			+ "93|92|91|90|86|84|82|81|66|65|64|63|62|61|60|58|57|56|55|54|53|52|51|49|48|47|46|45|44|43|"
			+ "41|40|39|36|34|33|32|30|27|20|7|1){1}[(]?[0-9]{1,14}[)]?[0-9]{1,14}";

	private static final Pattern dutchPhonePattern = Pattern.compile("[0-9|+]{1}[0-9.!?():\\-\\s]{9,23}");
	private static final Pattern internationPhonePattern = Pattern.compile(regex);

	private static final Pattern postPattern = Pattern.compile("\\s+\\d{4}\\s?[A-Z]{2}\\s+([a-zA-Z]+\\s{1}[a-zA-Z]+)?([a-zA-Z]+)?");
	private static final Pattern addressPattern = Pattern.compile("[a-zA-Z']+(\\s{1}[a-zA-Z']+)?\\s{1}\\d+((-{1}[a-zA-Z]{1})|[a-zA-Z]{1})?");

	private static final Pattern emailPattern = Pattern.compile("\\S+[@]{1}\\S+[\\.]+\\S+");

	public WorkerGoogle(String link, String site, ArrayList<String> proxies, RandomGenerator gen, TerminationLimit termination, MessagePanel messenger) {
		super(link, site, proxies, gen, termination, messenger);
	}

	@Override
	public Result call() throws Exception {

		Result res = new Result(link);

		res = this.processHit(res);

		URL host = new URL(link);
		try {
			String name = host.getHost().substring(host.getHost().indexOf(".")+1, host.getHost().lastIndexOf("."));
			res.setCompany(name);
		} catch (Exception e) {
			res.setCompany(host.getHost());
		}
		return res;
	}

	public Result processHit(Result result) throws Exception {
		WebBrowser web = new WebBrowser();

		try{
			HtmlPage page = web.getPage(result.getUrl());
			result = this.processContactPage(page.asText(), result);
			List<HtmlAnchor> anchorList = page.getAnchors();
			HashSet<String> contactUrls = new HashSet<String>();
			String host = page.getUrl().toString();
			int pageCounter = 0;			

			for(HtmlAnchor anchor : anchorList){
				if(anchor!=null){
					if(this.checkAnchor(anchor)) {
						String href = "";
						if(WebBrowser.isValid(host+anchor.getHrefAttribute())) {
							href = host+anchor.getHrefAttribute();
						}
						if(WebBrowser.isValid(anchor.getHrefAttribute())) {
							href = anchor.getHrefAttribute();
						}
						if(!contactUrls.contains(href) && !(href).equals(host)){
							contactUrls.add(href);
							try {
								if(result.getNumbers().size()>0 && pageCounter>10){
									break;
								}
								HtmlPage contactPage = anchor.click();
								result = this.processContactPage(contactPage.asText(), result);
								pageCounter++;
							} catch (Exception e) {}
						}

					}
				}
			}
		}finally{
			web.close();
		}
		return result;		
	}

	public Result processContactPage(String page, Result result){

		String body = page;

		if(this.containsEmails(body)){
			ArrayList<String> emails = this.getEmails(body);
			for(String email : emails){
				result.addEmail(email);
			}
		}

		String[] parts = body.split("\\r?\\n");
		int size = parts.length;

		for(int i=1; i<size; i++){
			int index = i;
			String bit = parts[index];
			String otherBit = parts[index-1];
			if(!bit.contains("fax") && !otherBit.contains("fax")){
				if(this.containsPostals(bit)){
					ArrayList<String> postals = this.getPostals(bit);
					ArrayList<String> adresses = new ArrayList<String>();
					for(int j=-1; j<=1; j++){
						if(index<size-1){
							String newBit = parts[index+j];
							if(this.containsAdress(newBit)){
								adresses.addAll(this.getAdresses(newBit));
							}
						}
					}
					if(postals.size()==adresses.size()){
						for(int k=0; k<postals.size(); k++){
							Adress adress = new Adress(adresses.get(k), postals.get(k));
							result.addAdress(adress);
						}
					}
				}

				if(this.containsNumbers(bit)){
					ArrayList<String> numbers = this.getPhoneNumbers(bit);
					for(String number : numbers){
						result.addNumber(number);
					}
				}
			}
		}
		return result;
	}

	public boolean containsAdress(String bit){
		Matcher matcher = WorkerGoogle.addressPattern.matcher(bit);
		if(matcher.find()){
			return true;
		}else{
			return false;
		}
	}

	public ArrayList<String> getAdresses(String bit){
		ArrayList<String> adresses = new ArrayList<String>();
		Matcher matcher = WorkerGoogle.addressPattern.matcher(bit);
		while(matcher.find()){
			String toAdd = bit.substring(matcher.start(), matcher.end());
			adresses.add(toAdd);
		}

		return adresses;
	}

	public boolean containsPostals(String bit){
		Matcher matcher = WorkerGoogle.postPattern.matcher(bit);
		if(matcher.find()){
			return true;
		}else{
			return false;
		}
	}

	public ArrayList<String> getPostals(String body){
		ArrayList<String> postals = new ArrayList<String>();
		Matcher matcher = WorkerGoogle.postPattern.matcher(body);
		while(matcher.find()){
			String toAdd = body.substring(matcher.start(), matcher.end());
			postals.add(toAdd);
		}

		return postals;
	}

	public boolean containsEmails(String bit){
		Matcher matcher = WorkerGoogle.emailPattern.matcher(bit);
		if(matcher.find()){
			return true;
		}else{
			return false;
		}
	}

	public ArrayList<String> getEmails(String body){
		ArrayList<String> emails = new ArrayList<String>();

		// Universal
		Matcher matcher = emailPattern.matcher(body);

		while(matcher.find()){
			String toAdd = body.substring(matcher.start(), matcher.end());
			emails.add(toAdd);
		}

		return emails;
	}

	public boolean containsNumbers(String bit){
		char[] arr = bit.toCharArray();
		int digitCounter = 0;
		boolean valid = false;
		for(Character c : arr){
			if(digitCounter>=10){
				valid = true;
			}
			if(Character.isDigit(c)){
				digitCounter++;
			}
			if(Character.isLetter(c)){
				digitCounter=0;
			}
		}

		if(digitCounter>=10){
			valid = true;
		}
		return valid;
	}

	public ArrayList<String> getPhoneNumbers(String body){
		ArrayList<String> numbers = new ArrayList<String>();

		// Dutch and international phone matchers
		Matcher dutchMatcher = WorkerGoogle.dutchPhonePattern.matcher(body);
		Matcher internationalMatcher = WorkerGoogle.internationPhonePattern.matcher(body);

		while(dutchMatcher.find()){
			String toAdd = body.substring(dutchMatcher.start(), dutchMatcher.end());
			if(this.containsNumbers(toAdd)){
				numbers.add(toAdd);
			}
		}
		while(internationalMatcher.find()){
			String toAdd = body.substring(internationalMatcher.start(), internationalMatcher.end());
			if(toAdd.length()>=10 && toAdd.length()<21){
				numbers.add(toAdd);
			}
		}
		return numbers;
	}

	public boolean checkAnchor(HtmlAnchor anchor){
		if(anchor.getHrefAttribute().contains("mailto")){
			return false;
		}
		if(anchor.asText().toLowerCase().contains("contact")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("kontakt")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("klantenservice")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("klanten service")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("connecter")){
			return true;
		}	
		if(anchor.asText().toLowerCase().contains("contatt")){
			return true;
		}	
		if(anchor.asText().toLowerCase().contains("contacto")){
			return true;
		}	
		if(anchor.asText().toLowerCase().contains("customer service")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("customerservice")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("servicio al cliente")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("servizio di assistenza")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("kundendienst")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("service clients")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("renseignements")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("coordonnées")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("conoscenza")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("estremi")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("recapito")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("coordinate")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("señas")){
			return true;
		}
		if(anchor.asText().toLowerCase().contains("over ons")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("contact")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("kontakt")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("klantenservice")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("klanten service")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("connecter")){
			return true;
		}	
		if(anchor.getHrefAttribute().toLowerCase().contains("contatt")){
			return true;
		}	
		if(anchor.getHrefAttribute().toLowerCase().contains("contacto")){
			return true;
		}	
		if(anchor.getHrefAttribute().toLowerCase().contains("customer service")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("customerservice")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("servicio al cliente")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("servizio di assistenza")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("kundendienst")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("service clients")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("renseignements")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("coordonnées")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("conoscenza")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("estremi")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("recapito")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("coordinate")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("señas")){
			return true;
		}
		if(anchor.getHrefAttribute().toLowerCase().contains("over ons")){
			return true;
		}
		return false;
	}
}
