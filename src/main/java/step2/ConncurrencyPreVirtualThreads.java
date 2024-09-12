package step2;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import common.CommonUtils;

public class ConncurrencyPreVirtualThreads {
	//What we'd use before loom
	final static ExecutorService executor = Executors.newFixedThreadPool(4);	
//	final static ExecutorService executor = Executors.newWorkStealingPool(4);
//	final static ExecutorService executor = Executors.newCachedThreadPool();
	
	public static void main(String... args) throws Exception {
		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new ConncurrencyPreVirtualThreads();
		String results = instance.runTasks();
		System.out.println(results);

		CommonUtils.waitForUser("Press enter to exit.");
		executor.shutdown();
	}
	
	private String runTasks() throws Exception {

		var tasks = executor.invokeAll(List.<Callable<String>>of(
				() -> CommonUtils.task("A", 500),
				() -> CommonUtils.task("B", 1500),
				() -> CommonUtils.task("C", 1000)
				));
		
		return tasks.stream()
				.filter(t -> t.state() == State.SUCCESS)
				.map(Future::resultNow)
				.collect(Collectors.joining(", ", "{ ", " }"));
	}
}