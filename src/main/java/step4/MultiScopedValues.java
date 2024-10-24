package step4;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class MultiScopedValues {
	final static ScopedValue<Integer> TIME = ScopedValue.newInstance();
	final static ScopedValue<String> NAME = ScopedValue.newInstance();

	public static void main(String... args) throws Throwable {
		MultiScopedValues instance = new MultiScopedValues();
		String results = instance.runTasks();
		System.out.println(results);
	}

	private String runTasks() throws Throwable {
		return ScopedValue.where(TIME, 1000).where(NAME, "hello").call(() -> {
			try (var scope = StructuredTaskScope.<String, Stream<Subtask<String>>>open(Joiner.allSuccessfulOrThrow())) {
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(TIME.get());
					result = NAME.get() + "a";
					System.out.println(result);
					return result;
				});
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(TIME.get());
					result = NAME.get() + "b";
					System.out.println(result);
					return result;
				});
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(TIME.get());
					result = NAME.get() + "c";
					System.out.println(result);
					return result;
				});
				scope.fork(() -> {
					String result;
					TimeUnit.MILLISECONDS.sleep(TIME.get());
					result = ScopedValue.where(NAME, "world").call(() -> {
						return NAME.get() + "d";
					});
					System.out.println(result);
					System.out.println(NAME.get() + "d");
					return result;
				});

				return scope.join().map(f -> f.get()).collect(Collectors.joining(", ", "{ ", " }"));
			} catch (Exception e) {
				throw e;
			}
		});
	}
}
