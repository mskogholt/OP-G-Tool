package tests;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import managers.ProxyManager;
import models.WebBrowser;

public class ProxyTest {

	public static void main(String[] args) throws Exception {
		
//		http://newproxy.ninja/
//		http://www.theproxy.link/
//		http://www.hollandproxy.eu/
//		http://www.shinyproxy.eu/
//		http://applepieproxy.xyz
//		http://xtcsoul.net/
//		http://www.bramka-proxy.pl/

		WebBrowser web = new WebBrowser();
		String proxy = "http://www.topnewproxy.gq/";
		String link = "https://www.detelefoongids.nl/bakker/rotterdam/3-1/?page=1";
		DomElement goButton = ProxyManager.getProxy(web, proxy, link);

		HtmlPage page = goButton.click();

		if(page.asText().contains("The site you are attempting to browse is on a secure connection. This proxy is not on a secure connection.")) {
			page = ProxyManager.ignoreWarning(page, web);
		}
		
		System.out.println(page.asText());

	}

}
