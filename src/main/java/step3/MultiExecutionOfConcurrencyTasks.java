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
		CommonUtils.waitForUser("Press enter to continue.");
		final var instance = new MultiExecutionOfConcurrencyTasks();
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			executor.invokeAll(List.<Callable<String>>of(
			   () -> {
				return instance.callWebServices(true);
			}, () -> {
				return instance.callWebServices(false);
			}, () -> {
				return instance.callWebServices(false);
			}));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String callWebServices(boolean throwError) throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			var tasks = executor.invokeAll(List.<Callable<String>>of(
					() -> CommonUtils.task("A", 4500),
					() -> CommonUtils.task("B", 5500), 
					() -> CommonUtils.task("C", 5000, throwError)));

			return tasks.stream().map(Future::resultNow).collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}