/**
 * 
 */
package workers.links;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import containers.Query;
import containers.concurrency.RandomGenerator;
import graphicalinterfaces.MessagePanel;
import models.TerminationLimit;

/**
 * @author martin.skogholt
 *
 */
public class LinkWorkerTest extends LinkWorker{

	public LinkWorkerTest(Query query, String site, ArrayList<String> proxies, RandomGenerator gen,
			TerminationLimit termination, MessagePanel messenger) {
		super(query, site, proxies, gen, termination, messenger);
	}


	@Override
	public ArrayList<String> call() throws Exception {

		if(ThreadLocalRandom.current().nextDouble()>0.9) {
			throw new Exception("Test Exception Occurred");
		}
		
		ArrayList<String> links = new ArrayList<String>();

		if(termination.getType().equals("Page Limit")) {
			for(int i=0; i<termination.getLimit(); i++) {
				links.add("http://www.test"+i+".nl");
			}
		}else {
			for(int i=0; i<1000; i++) {
				links.add("http://www.test"+i+".nl");
			}
		}
		
		Random rand = new Random();
		
		Thread.sleep(rand.nextInt(100));
		
		return links;
	}

}
