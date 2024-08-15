package step2;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fly.us.WebServiceHelper;

public class FuturesSolutionPreVirtualThreads {
	//What we'd use before loom
	final static ExecutorService executor = Executors.newFixedThreadPool(4);	
//	final static ExecutorService executor = Executors.newWorkStealingPool(4);
//	final static ExecutorService executor = Executors.newCachedThreadPool();
	
	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to continue.");

		var instance = new FuturesSolutionPreVirtualThreads();
		String results = instance.callWebServices();
		System.out.println(results);

		WebServiceHelper.waitForUser("Press enter to exit.");
		executor.shutdown();
	}
	
	private String callWebServices() throws Exception {

		var tasks = executor.invokeAll(List.<Callable<String>>of(
				() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(500);
					result = "a";
					System.out.println(result);
					return result;
				},
				() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(500);
					result = "b";
					System.out.println(result);
					return result;
				},
				() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(500);
					result = "c";
					System.out.println(result);
					return result;
				}
				));
		
		return tasks.stream()
				.filter(t -> t.state() == State.SUCCESS)
				.map(Future::resultNow)
				.collect(Collectors.joining(", ", "{ ", " }"));
	}
}