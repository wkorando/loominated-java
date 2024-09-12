package step3;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future.State;
import java.util.stream.Collectors;

import com.fly.us.WebServiceHelper;

import common.CommonUtils;

public class ConcurrencyWithVirtualThreads {

	public static void main(String... args) throws Exception {
		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new ConcurrencyWithVirtualThreads();
		String results = instance.callWebServices();
		System.out.println(results);
	}

	private String callWebServices() throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			
			var tasks = executor.invokeAll(List.<Callable<String>>of(
					() -> CommonUtils.task("A", 500),
					() -> CommonUtils.task("B", 1500),
					() -> CommonUtils.task("C", 1000)
					));
			
			return tasks.stream()
					.map(Future::resultNow)
					.collect(Collectors.joining(", ", "{ ", " }"));
		}
	}
}