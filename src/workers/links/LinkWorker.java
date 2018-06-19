/**
 * 
 */
package workers.links;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import containers.Query;
import containers.concurrency.RandomGenerator;
import graphicalinterfaces.MessagePanel;
import models.TerminationLimit;

/**
 * @author martin.skogholt
 *
 */
public abstract class LinkWorker implements Callable<ArrayList<String>>{

	protected Query query;
	protected String site;
	protected ArrayList<String> proxies;
	protected RandomGenerator gen;
	protected TerminationLimit termination;
	protected MessagePanel messenger;
	
	public LinkWorker(Query query, String site, ArrayList<String> proxies, RandomGenerator gen, TerminationLimit termination, MessagePanel messenger) {
		this.query = query;
		this.site = site;
		this.proxies = proxies;
		this.gen = gen;
		this.termination = termination;
		this.messenger = messenger;
	}

	@Override
	public abstract ArrayList<String> call() throws Exception ;

}
