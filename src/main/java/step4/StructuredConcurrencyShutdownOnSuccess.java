package step4;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.TimeUnit;

import com.fly.us.WebServiceHelper;

public class StructuredConcurrencyShutdownOnSuccess {

	public static void main(String... args) throws Throwable {

		WebServiceHelper.waitForUser("Press enter to continue.");

		StructuredConcurrencyShutdownOnSuccess instance = new StructuredConcurrencyShutdownOnSuccess();
		String result = instance.callWebServices();
		System.out.println(result);

		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		try (var scope = StructuredTaskScope.<String, String>open(Joiner.anySuccessfulResultOrThrow())) {

			scope.fork(() -> {
				TimeUnit.MILLISECONDS.sleep(500);
				System.out.println("a");
				return "a";
			});
			scope.fork(() -> {
				TimeUnit.MILLISECONDS.sleep(1500);
				System.out.println("b");
				return "b";
			});
			scope.fork(() -> {
				TimeUnit.MILLISECONDS.sleep(1000);
				System.out.println("c");
				return "c";
			});
			return scope.join();
		} catch (Exception e) {
			throw e;
		}
	}

}
