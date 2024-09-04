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

public class MultiExecutionOfConcurrencyTasks {

	public static void main(String... args) throws Exception {
//		WebServiceHelper.waitForUser("Press enter to continue.");
		final var instance = new MultiExecutionOfConcurrencyTasks();
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			var tasks = executor.invokeAll(List.<Callable<String>>of(
			() -> {
				return instance.callWebServices(true);
			}, 
			() -> {
				return instance.callWebServices(false);
			},
			() -> {
				return instance.callWebServices(false);
			}));

			System.out.println(tasks.stream().map(Future::resultNow).collect(Collectors.joining(", ", "{ ", " }")));
		}

//		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices(boolean stall) throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			var tasks = executor.invokeAll(List.<Callable<String>>of(
				() -> CommonUtils.task("A", 500),
				() -> CommonUtils.task("B", 1500),
				() -> CommonUtils.task("C", 1000)
				));

			return tasks.stream().map(Future::resultNow).collect(Collectors.joining(", ", "{ ", " }"));
		}
	}
}