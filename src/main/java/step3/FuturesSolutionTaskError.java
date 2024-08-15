package step3;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fly.us.WebServiceHelper;

public class FuturesSolutionTaskError {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to continue.");

		var instance = new FuturesSolutionTaskError();
		String result = instance.callWebServices();
		System.out.println(result);
		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			
			var tasks = executor.invokeAll(List.<Callable<String>>of(
					() -> {
						String result;
						TimeUnit.MILLISECONDS.sleep(1500);
						result = "a";
						System.out.println(result);
						return result;
					},
					() -> {
						String result;
						TimeUnit.MILLISECONDS.sleep(1500);
						result = "b";
						System.out.println(result);
						return result;
					},
					() -> {
						String result;
						TimeUnit.MILLISECONDS.sleep(1500);
						result = "c";
						System.out.println(result);
						return result;
					},
					() -> {
						TimeUnit.MILLISECONDS.sleep(100);
						RuntimeException e = new RuntimeException("Error!");
						e.printStackTrace();
						throw e;
					}));
			
			return tasks.stream()
					.map(Future::resultNow)
					.collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}
	}
}