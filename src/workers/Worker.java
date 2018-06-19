/**
 * 
 */
package workers;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import containers.Result;
import containers.concurrency.RandomGenerator;
import graphicalinterfaces.MessagePanel;
import models.TerminationLimit;

/**
 * @author martin.skogholt
 *
 */
public abstract class Worker implements Callable<Result>{

	protected String link;
	protected String site;
	protected ArrayList<String> proxies;
	protected RandomGenerator gen;
	protected TerminationLimit termination;
	protected MessagePanel messenger;
	
	public Worker (String link, String site, ArrayList<String> proxies, RandomGenerator gen, 
			TerminationLimit termination, MessagePanel messenger) {
		this.link = link;
		this.site = site;
		this.proxies = proxies;
		this.gen = gen;
		this.termination = termination;
		this.messenger = messenger;
	}

	@Override
	public abstract Result call() throws Exception;
	
}
