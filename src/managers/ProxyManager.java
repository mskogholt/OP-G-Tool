/**
 * 
 */
package managers;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import models.WebBrowser;

/**
 * @author martin.skogholt
 *
 */
public class ProxyManager {
	
	public static DomElement getProxy(WebBrowser web, String proxyLink, String link) throws Exception {
		DomElement goButton = null;
		
		if(proxyLink.contains("webanonymizer")){
			// Get the first page
			HtmlPage page = web.getPage("http://webanonymizer.org/");

			HtmlInput input = page.getHtmlElementById("main-url-input");
			input.setValueAttribute(link);

			goButton = page.getFirstByXPath("/html/body/div[2]/div/form/div/div[3]/button[1]");
		}
		if(proxyLink.contains("filterbypass")){

			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getElementByName("k");
			input.setValueAttribute(link);

			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");
		}
		if(proxyLink.contains("unblockmyweb")){

			// Get the first page
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getElementByName("u");
			input.setValueAttribute(link);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");
			page = goButton.click();

			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);

			goButton = page.getFirstByXPath("/html/body/div[1]/form/input[2]");
			
		}
		if(proxyLink.contains("xitenow")){

			// Get the first page
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getHtmlElementById("input");
			input.setValueAttribute(link);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");

			page = goButton.click();

			goButton = page.getFirstByXPath(page.getHtmlElementById("wrapper").getCanonicalXPath()+"/form/input[2]");

			page = goButton.click();

			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);

			goButton = page.getFirstByXPath(page.getHtmlElementById("include").getCanonicalXPath() + "/form/p[1]/input[2]");
			
		}
		if(proxyLink.contains("fishproxy")){
			web.getOptions().setJavaScriptEnabled(true);
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getHtmlElementById("input");
			input.setValueAttribute(link);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");

			page = goButton.click();

			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);

			goButton = page.getFirstByXPath("/html/body/center/div/form/input[2]");
		}
		if(link.contains("proxfree")){
			// Proxfree
			// Get the first page
			HtmlPage page = web.getPage("https://www.proxfree.com/");
								
			HtmlSelect select = page.getElementByName("pfipDropdown");
			int max = select.getOptionSize();
			double index = Math.random()*((double) max);
			int selectedIndex = Math.min((max-1), ((int) index));
			page = select.setSelectedAttribute(select.getOption(selectedIndex), true);
			
			HtmlInput input = page.getElementByName("get");
			input.setValueAttribute(link);
			
			goButton = page.getFirstByXPath("/html/body/div[2]/div/form/input[2]");
		}
		if(proxyLink.contains("newproxy.ninja")){
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getHtmlElementById("input");
			input.setValueAttribute(link);

			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");
					
		}
		if(proxyLink.contains("theproxy.link/")){
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getHtmlElementById("input");
			input.setValueAttribute(link);

			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");
					
		}
		if(proxyLink.contains("http://www.hollandproxy.eu/")){
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getHtmlElementById("url");
			input.setValueAttribute(link);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");
					
			page = goButton.click();
			if(page.asText().contains("The site you are attempting to browse is on a secure connection. This proxy is not on a secure connection.")) {
				page = ProxyManager.ignoreWarning(page, web);
			}
			
			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);
	
			goButton = page.getFirstByXPath(checkBox.getParentNode().getParentNode().getCanonicalXPath()+"/p[1]/input[2]");
		}
		if(proxyLink.contains("shinyproxy")){
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getHtmlElementById("url");
			input.setValueAttribute(link);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");
					
			page = goButton.click();
			if(page.asText().contains("The site you are attempting to browse is on a secure connection. This proxy is not on a secure connection.")) {
				page = ProxyManager.ignoreWarning(page, web);
			}
			
			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);
	
			goButton = page.getFirstByXPath(checkBox.getParentNode().getParentNode().getCanonicalXPath()+"/p[1]/input[2]");
				
		}
		if(proxyLink.contains("applepieproxy")){
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getHtmlElementById("input");
			input.setValueAttribute(link);

			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");
					
		}
		if(proxyLink.contains("xtcsoul")){
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getHtmlElementById("input");
			input.setValueAttribute(link);

			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");
					
		}
		if(proxyLink.contains("bramka-proxy")){
			HtmlPage page = web.getPage(proxyLink);

			HtmlInput input = page.getHtmlElementById("input");
			input.setValueAttribute(link);

			HtmlInput checkBox = page.getHtmlElementById("encodeURL");
			checkBox.setChecked(false);

			goButton = page.getFirstByXPath(input.getParentNode().getCanonicalXPath()+"/input[2]");
					
		}
		return goButton;
	}
	
	public static HtmlPage ignoreWarning(HtmlPage page, WebBrowser web) throws Exception {
	
		for(HtmlElement element : page.getHtmlElementDescendants()) {
			if(element.asText().equals("Continue anyway...")) {
				page = element.click();
				break;
			}
		}
		
		return page;
	}
}
