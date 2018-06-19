/**
 * 
 */
package containers.concurrency;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Martin
 *
 */
public class RandomGenerator {

	private ArrayList<Integer> indexes = new ArrayList<Integer>();
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock readLock = readWriteLock.readLock();
	private final Lock writeLock = readWriteLock.writeLock();
	private final Random rand = new Random();

	public RandomGenerator(int max){
		for(int i=0; i<max; i++){
			indexes.add(i);
		}
	}

	public void addIndex(int index){
		if(indexes.size()<=200){
			writeLock.lock();
			try{
				indexes.add(index);
			} finally{
				writeLock.unlock();
			}
		}
	}

	public void removeIndex(Integer index){
		writeLock.lock();
		try{
			indexes.remove(index);
			if(!indexes.contains(index)){
				indexes.add(index);
			}
		}finally{
			writeLock.unlock();
		}
	}
	
	public int getIndex(){
		readLock.lock();
		int index = 0;
		try{
			index = indexes.get(rand.nextInt(indexes.size()));
		}finally{
			readLock.unlock();
		}
		return index;
	}
}
