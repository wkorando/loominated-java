package step2;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import common.CommonUtils;

public class Part2ConcurrencyShutdownOnSuccess {

	public static void main(String... args) throws Exception {
		CommonUtils.waitForUser("Press enter to continue.");

		Part2ConcurrencyShutdownOnSuccess instance = new Part2ConcurrencyShutdownOnSuccess();
		String result = instance.runTasks();
		System.out.println(result);

		CommonUtils.waitForUser("Press enter to exit.");

	}

	private String runTasks() throws Exception {

		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			var tasks = List.<Callable<String>>of(
					() -> CommonUtils.task("A", 500),
					() -> CommonUtils.task("B", 1500),
					() -> CommonUtils.task("C", 100000)
					);

			return executor.invokeAny(tasks);

		} catch (Exception e) {

			throw e;

		}
		
	}
}