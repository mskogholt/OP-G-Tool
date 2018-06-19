/**
 * 
 */
package containers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author martin.skogholt
 *
 */
public class Task<E> implements Future<E>{
	
	private Future<E> future;
	private Query query;
	private String link;
	private int attempts = 0;
	
	public Task(Future<E> fut, Query query) {
		this.future = fut;
		this.query = query;
	}

	public Task(Future<E> fut, String link) {
		this.future = fut;
		this.link = link;
	}
	
	public int getAttempts() {
		return this.attempts;
	}
	
	public void addAttempts(int extraAttempts) {
		this.attempts += extraAttempts;
	}
	
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	
	public String getLink() {
		return this.link;
	}
	
	public Query getQuery() {
		return this.query;
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	@Override
	public E get() throws InterruptedException, ExecutionException {
		return future.get();
	}

	@Override
	public E get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return future.get(timeout, unit);
	}

	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return future.isDone();
	}
}
