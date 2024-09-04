package step3;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fly.us.WebServiceHelper;

import common.CommonUtils;

public class ConcurrencyShutdownOnSuccess {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to continue.");

		ConcurrencyShutdownOnSuccess instance = new ConcurrencyShutdownOnSuccess();
		String result = instance.callWebServices();
		System.out.println(result);

		WebServiceHelper.waitForUser("Press enter to exit.");

	}

	private String callWebServices() throws Exception {

		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			var tasks = List.<Callable<String>>of(
					() -> CommonUtils.task("A", 500),
					() -> CommonUtils.task("B", 1500),
					() -> CommonUtils.task("C", 1000)
					);

			return executor.invokeAny(tasks);

		} catch (Exception e) {

			throw e;

		}
	}
}