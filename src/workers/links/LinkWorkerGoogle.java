/**
 * 
 */
package workers.links;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import containers.Query;
import containers.concurrency.RandomGenerator;
import graphicalinterfaces.MessagePanel;
import managers.ProxyManager;
import models.TerminationLimit;
import models.WebBrowser;

/**
 * @author martin.skogholt
 *
 */
public class LinkWorkerGoogle extends LinkWorker{

	public LinkWorkerGoogle(Query query, String site, ArrayList<String> proxies, RandomGenerator gen, TerminationLimit termination, MessagePanel messenger) {
		super(query, site, proxies, gen, termination, messenger);
	}

	@Override
	public ArrayList<String> call() throws Exception {

		String queryText = query.getWhat()+" "+query.getWhere().trim();
		ArrayList<String> urls = new ArrayList<String>();
		int tries = 0;
		messenger.setMessage("Initializing Google Collector...");
		while(urls.size()<1 && tries<10) {
			try {
				messenger.setMessage("Starting attempt " + (tries+1) + " out of 10");
				Thread.sleep(1000);
				urls.addAll(this.getUrls(this.googleSearch(queryText)));
			} catch (Exception e) {
				System.out.println(e);
			}
			finally {
				tries++;
			}
		}
		return urls;
	}

	public HashSet<String> getUrlsAsText(String page){
		String[] splitted = page.split("\\r?\\n");
		HashSet<String> urls = new HashSet<String>();
		for(String url : splitted){
			if(url.contains("www.") || url.contains("http://")){
				url = url.replace("Adv.", "");
				if(url.endsWith("/")){
					url = url.substring(0, url.length()-1);
				}
				if(!url.contains("www.google.")){
					urls.add("http://"+url);
				}
			}
		}
		return urls;
	}

	public boolean check(String xml){
		if(this.getGoogleUrls(xml).size()<4){
			return false;
		}else{
			return true;
		}
	}

	public HashSet<String> getGoogleUrls(String pageText){

		HashSet<String> urls = new HashSet<String>();
		String asText = "";
		String[] splitted = pageText.split("\\r?\\n");

		boolean start = false;
		for(String split : splitted){
			if(!split.contains("<") && !split.contains(">") && !start && split.contains("www")){
				asText = asText + split.trim() + "\n";
			}else{
				if(start){
					if(split.contains("</cite>")){
						start = false;
						asText = asText+"\n";
					}else{
						if(!split.contains("<b>") && !split.contains("</b>")){
							split = split.trim();
							if(split.endsWith("\n") || split.endsWith("\r")){
								split = split.substring(0, split.length()-1);
							}
							if(split.startsWith("\n") || split.startsWith("\r")){
								split = split.substring(1, split.length());
							}
							asText = asText+split.trim();
						}
					}
				}
			}
			if(split.contains("<cite")){
				start = true;
			}
		}

		String[] refinedSplit = asText.split("\\r?\\n");
		for(String split : refinedSplit){
			if(split.startsWith("www") || split.startsWith("http")){
				if(split.startsWith("www")){
					split = "http://" + split;
				}
				if(!split.contains("www.google.")){
					split = split.replace("https://", "http://");
					split = split.replace("amp;", "");
					if(!split.contains("prox")){
						urls.add(split);
					}
				}
			}
		}
		return urls;
	}

	public HashSet<String> googleSearch(String queryText) throws Exception {

		WebBrowser web = new WebBrowser();

		HashSet<String> urls = new HashSet<String>();
		int selectLink = this.gen.getIndex();
		try{
			String link = proxies.get(selectLink);
			messenger.setMessage("Trying to connect with proxy...");

			DomElement goButton = ProxyManager.getProxy(web, link, site);

			messenger.setMessage("Proxy Connected!");

			HtmlPage page = goButton.click();
			// Gets the google search bar as htmlinput and inputs the query to be googled.
			HtmlInput input = page.getElementByName("q");

			input.setValueAttribute(queryText);

			HtmlSubmitInput submit = page.getElementByName("btnK");

			HtmlPage googlePage = submit.click();

			boolean done = false;
			int counter = 0;

			while(!done){
				urls.addAll(getGoogleUrls(googlePage.asXml()));

				if(urls.size()<4 && !this.checkPage(googlePage.asText()) && counter==0){
					this.gen.removeIndex(selectLink);
					throw new ElementNotFoundException(link, queryText, "Result: " + urls.size());
				}			

				// Try to click on to the next page
				try{
					HtmlTable table = (HtmlTable) googlePage.getHtmlElementById("nav");
					HtmlTableBody body = table.getBodies().get(0);
					HtmlTableRow row = body.getRows().get(0);
					List<HtmlTableCell> cells = row.getCells();
					HtmlTableDataCell cell = (HtmlTableDataCell) cells.get(cells.size()-1);
					HtmlAnchor nextPage = (HtmlAnchor) cell.getFirstChild();
					googlePage = nextPage.click();
				} catch (ElementNotFoundException | ClassCastException e){
					done = true;
					break;
				}
				counter++;
				if(termination.getType().equals("Page Limit")) {
					if(termination.terminate(counter)) {
						done = true;
						break;
					}
				}
				messenger.setMessage("Collected google page " + counter);
				if(termination.getType().equals("Page Limit")) {
					double prog = ((double) counter)/((double) termination.getLimit());
					prog = prog * 100.0;
				}
			}
		} catch (Exception e){
			this.gen.removeIndex(selectLink);
			throw e;
		}finally{
			web.close();
		}
		this.gen.addIndex(selectLink);
		web.close();
		return urls;
	}

	public boolean checkPage(String text){
		if(text.contains("heeft geen overeenkomstige documenten opgeleverd")){
			return true;
		}
		if(text.contains("did not match any documents")){
			return true;
		}
		if(text.contains("non ha prodotto risultati in nessun documento")){
			return true;
		}
		if(text.contains("bereinstimmenden Dokumente gefunden")){
			return true;
		}
		if(text.contains("no obtuvo ningún resultado")){
			return true;
		}
		if(text.contains("ucun document ne correspond aux termes de re")){
			return true;
		}
		return false;
	}

	public ArrayList<String> getUrls(HashSet<String> urls){
		HashSet<String> urlSet = new HashSet<String>();
		for(String url : urls){
			try{
				URL link = new URL(url);
				urlSet.add("http://"+link.getHost());
			} catch (MalformedURLException e){
				try{
					URL link = new URL(url.substring(0, url.indexOf("/")));
					urlSet.add("http://"+link.getHost());
				} catch (MalformedURLException error){}
			}
		}

		ArrayList<String> urlList = new ArrayList<String>();
		urlList.addAll(urlSet);
		return urlList;
	}
}
