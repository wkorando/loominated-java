package step3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fly.us.WebServiceHelper;

import common.CommonUtils;

public class ConcurrencyShutdownOnError {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to continue.");

		ConcurrencyShutdownOnError instance = new ConcurrencyShutdownOnError();
		String result = instance.callWebServices();
		System.out.println(result);

		WebServiceHelper.waitForUser("Press enter to exit.");

	}

	private String callWebServices() throws Exception {

		var tasks = List.<Callable<String>>of(
				() -> CommonUtils.task("A", 500),
				() -> CommonUtils.task("B", 1500),
				() -> CommonUtils.task("C", 1000),
				() -> CommonUtils.task("B", 100, true)
				);
		
		
		var done = new Semaphore(0);
		var result = new ArrayList<Future<String>>();
		var executorService = Executors.newVirtualThreadPerTaskExecutor();
		Exception exc = null;
		try {
			for (var task : tasks)
				result.add(executorService.submit(() -> {
					try {
						var r = task.call();
						done.release(1);
						return r;
					} catch (Throwable t) {
						done.release(tasks.size());
						throw t;
					}
				}));
			done.acquire(tasks.size());
		} catch (InterruptedException | RejectedExecutionException e) {
			exc = e;
		} finally {
			executorService.shutdownNow();
			InterruptedException interrupted = null;
			while (true) {
				try {
					if (executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS))
						break;
				} catch (InterruptedException e) {
					if (interrupted == null)
						interrupted = e;
				}
			}
			if (exc != null) {
				if (interrupted != null) {
					exc.addSuppressed(interrupted);
				}
				if (exc instanceof InterruptedException ie)
					throw ie;
				else if (exc instanceof RuntimeException re)
					throw re;
			} else if (interrupted != null) {
				throw interrupted;
			}
		}
				
		return result.stream()
				.map(Future::resultNow)
				.collect(Collectors.joining(", ", "{ ", " }"));
	}
}