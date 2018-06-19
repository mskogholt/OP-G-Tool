/**
 * 
 */
package models;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Martin
 *
 */
public class WebBrowser extends WebClient {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WebBrowser(){
		super(BrowserVersion.CHROME);
		this.getOptions().setThrowExceptionOnFailingStatusCode(false);
		this.getOptions().setThrowExceptionOnScriptError(false);
		this.getOptions().setJavaScriptEnabled(false);
		this.getOptions().setCssEnabled(false);
		this.getOptions().setUseInsecureSSL(true);
	}

	public void setTimeout(int timeout){
		this.getOptions().setTimeout(timeout);
	}
	
	@SuppressWarnings("unused")
	public static boolean isValid(String url) {
		boolean valid = false;
		try {
			URL test = new URL(url);
			valid = true;
		} catch (MalformedURLException e) {}
		return valid;
	}
}
