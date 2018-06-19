/**
 * 
 */
package workers;

import java.util.ArrayList;

import containers.concurrency.RandomGenerator;
import graphicalinterfaces.MessagePanel;
import models.TerminationLimit;

/**
 * @author martin.skogholt
 *
 */
public class WorkerFactory {

	public static Worker getWorker(String link, String site, ArrayList<String> proxies, 
			RandomGenerator gen, TerminationLimit termination, MessagePanel messenger) {
		if(site.contains("google")) {
			return new WorkerGoogle(link, site, proxies, gen, termination, messenger);
		}
		if(site.contains("test")) {
			return new WorkerTest(link, site, proxies, gen, termination, messenger);
		}
		return null;
	}
}
