package step2;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import common.CommonUtils;

public class ConcurrencyWithTaskErrorAndErrorHandling {

	public static void main(String... args) throws Exception {
		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new ConcurrencyWithTaskErrorAndErrorHandling();
		String result = instance.runTasks();
		System.out.println(result);
		CommonUtils.waitForUser("Press enter to exit.");
	}

	private String runTasks() throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			
			var tasks =  executor.invokeAll(List.<Callable<String>>of(
					() -> CommonUtils.task("A", 4500),
					() -> CommonUtils.task("B", 500),
					() -> CommonUtils.task("C", 1000),
					() -> CommonUtils.task("D", 100, true)
					));
			return tasks.stream()
					.map(f -> {
							try{
								return f.get();
							}catch(Exception e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
						})
					.collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}
	}
}