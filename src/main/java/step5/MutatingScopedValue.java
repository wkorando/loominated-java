package step5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fly.us.WebServiceHelper;

public class MutatingScopedValue {
	final static ScopedValue<List<String>> CACHE = ScopedValue.newInstance();

	public static void main(String... args) throws Throwable {

		WebServiceHelper.waitForUser("Press enter to continue.");

		MutatingScopedValue instance = new MutatingScopedValue();
		String results = instance.callWebServices();
		System.out.println(results);
		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		List<String> cache = new ArrayList<String>();
		cache.add("HELLO");
		return ScopedValue.where(CACHE, cache).call(() -> {
			try (var scope = StructuredTaskScope.<String, Stream<Subtask<String>>>open(Joiner.allSuccessfulOrThrow())) {
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(1000);
					result = CACHE.get().getFirst() + "a";
					System.out.println(result);
					return result;
				});
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(1000);
					cache.set(0, "world");
					result = CACHE.get().getFirst() + "b";
					System.out.println(result);
					return result;
				});
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(1000);
					result = CACHE.get().getFirst() + "c";
					System.out.println(result);
					return result;
				});
				

				return scope.join().map(Subtask::get).collect(Collectors.joining(", ", "{ ", " }"));
			} catch (Exception e) {
				throw e;
			}
		});
	}

}
