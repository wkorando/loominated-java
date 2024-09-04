package step3;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fly.us.WebServiceHelper;

import common.CommonUtils;

public class ConcurrencyTaskError {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to continue.");

		var instance = new ConcurrencyTaskError();
		String result = instance.callWebServices();
		System.out.println(result);
		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			
			var tasks =  executor.invokeAll(List.<Callable<String>>of(
					() -> CommonUtils.task("A", 500),
					() -> CommonUtils.task("B", 1500),
					() -> CommonUtils.task("C", 1000),
					() -> CommonUtils.task("B", 100, true)
					));
			
			return tasks.stream()
					.map(Future::resultNow)
					.collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}
	}
}