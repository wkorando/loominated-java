package step3;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import common.CommonUtils;

public class ConcurrencyWithTaskError {

	public static void main(String... args) throws Exception {
		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new ConcurrencyWithTaskError();
		String result = instance.runTasks();
		System.out.println(result);
		CommonUtils.waitForUser("Press enter to exit.");
	}

	private String runTasks() throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			
			var tasks =  executor.invokeAll(List.<Callable<String>>of(
					() -> CommonUtils.task("A", 500),
					() -> CommonUtils.task("B", 1500),
					() -> CommonUtils.task("C", 1000),
					() -> CommonUtils.task("D", 100, true)
					));
//			
			
			return tasks.stream()
					.map(Future::resultNow)
					.collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}
	}
}