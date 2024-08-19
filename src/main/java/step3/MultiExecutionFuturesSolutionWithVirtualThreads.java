package step3;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future.State;
import java.util.stream.Collectors;

import com.fly.us.WebServiceHelper;

public class MultiExecutionFuturesSolutionWithVirtualThreads {

	public static void main(String... args) throws Exception {
//		WebServiceHelper.waitForUser("Press enter to continue.");
		final var instance = new MultiExecutionFuturesSolutionWithVirtualThreads();
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

			var tasks = executor.invokeAll(List.<Callable<String>>of(() -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(500);
				result = "a";
				System.out.println(result);
				return result;
			}, () -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(500);
				result = "b";
				System.out.println(result);
				return result;
			}, () -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(500);
				result = "c";
				System.out.println(result);
				return result;
			}, () -> {
				if(stall) {
					TimeUnit.MILLISECONDS.sleep(500000);
				}
				String result;
				TimeUnit.MILLISECONDS.sleep(500);
				result = "d";
				System.out.println(result);
				return result;
			}));

			return tasks.stream().map(Future::resultNow).collect(Collectors.joining(", ", "{ ", " }"));
		}
	}
}