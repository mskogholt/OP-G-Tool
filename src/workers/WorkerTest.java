/**
 * 
 */
package workers;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import containers.Adress;
import containers.Result;
import containers.concurrency.RandomGenerator;
import graphicalinterfaces.MessagePanel;
import models.TerminationLimit;

/**
 * @author martin.skogholt
 *
 */
public class WorkerTest  extends Worker{

	public WorkerTest(String link, String site, ArrayList<String> proxies, RandomGenerator gen,
			TerminationLimit termination, MessagePanel messenger) {
		super(link, site, proxies, gen, termination, messenger);
	}

	@Override
	public Result call() throws Exception {
		
		Thread.sleep(1000);
		
		if(ThreadLocalRandom.current().nextDouble()>0.9) {
			throw new Exception("Test Exception Occurred");
		}
		
		Result res = new Result(link);
		res.setCompany("Martins Test Bedrijf");
		res.setWhat("Test");
		res.setWhere("Test");
		
		res.addEmail("test@test.com");
		res.addEmail("test@test.nl");
		
		res.addNumber("06test01");
		res.addNumber("06test02");
		res.addNumber("06test03");
		
		Adress ad1 = new Adress("testweg 1","3035 Test","Teststad");
		Adress ad2 = new Adress("testweg 2","3035 Test","Teststad");
		Adress ad3 = new Adress("testweg 3","3035 Test","Teststad");
		
		res.addAdress(ad1);
		res.addAdress(ad2);
		res.addAdress(ad3);
		
		return res;
	}

}
