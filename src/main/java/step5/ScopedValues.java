package step5;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class ScopedValues {
	final static ScopedValue<Integer> TIME = ScopedValue.newInstance();
	public static void main(String... args) throws Throwable {

		CommonUtils.waitForUser("Press enter to continue.");

		ScopedValues instance = new ScopedValues();
		String results = instance.callWebServices();
		System.out.println(results);
		CommonUtils.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		return ScopedValue.<Integer>where(TIME, 1000).call(() -> {
			try (var scope = StructuredTaskScope.<String, Stream<Subtask<String>>>open(Joiner.allSuccessfulOrThrow())) {
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(TIME.get());
					result = "a";
					System.out.println(result);
					return result;
				});
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(TIME.get());
					result = "b";
					System.out.println(result);
					return result;
				});
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(TIME.get());
					result = "c";
					System.out.println(result);
					return result;
				});
				
				return scope.join()
						.map(Subtask::get)
						.collect(Collectors.joining(", ", "{ ", " }"));
			} catch (Exception e) {
				throw e;
			}
		});
	}

}
