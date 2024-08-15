package step3;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fly.us.WebServiceHelper;

public class FuturesSolutionShutdownOnSuccess {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to continue.");

		FuturesSolutionShutdownOnSuccess instance = new FuturesSolutionShutdownOnSuccess();
		String result = instance.callWebServices();
		System.out.println(result);

		WebServiceHelper.waitForUser("Press enter to exit.");

	}

	private String callWebServices() throws Exception {

		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			var tasks = List.<Callable<String>>of(
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
				TimeUnit.MILLISECONDS.sleep(500);
				result = "c";
				System.out.println(result);
				return result;
			});

			return executor.invokeAny(tasks);

		} catch (Exception e) {

			throw e;

		}
	}
}