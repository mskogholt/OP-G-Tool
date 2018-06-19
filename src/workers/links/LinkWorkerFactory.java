/**
 * 
 */
package workers.links;

import java.util.ArrayList;

import containers.Query;
import containers.concurrency.RandomGenerator;
import graphicalinterfaces.MessagePanel;
import models.TerminationLimit;

/**
 * @author martin.skogholt
 *
 */
public class LinkWorkerFactory {

	public static LinkWorker getLinkWorker(Query query, String site, ArrayList<String> proxies, RandomGenerator gen, TerminationLimit termination, MessagePanel messenger) {
		if(site.contains("google")) {
			return new LinkWorkerGoogle(query, site, proxies, gen, termination, messenger);
		}
		if(site.contains("test")) {
			return new LinkWorkerTest(query, site, proxies, gen, termination, messenger);
		}
		return null;
	}
	
}
