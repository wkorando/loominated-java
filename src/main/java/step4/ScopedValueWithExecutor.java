package step4;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ScopedValueWithExecutor {
	private static final ScopedValue<String> NAME = ScopedValue.newInstance();

	public static void main(String[] args) throws InterruptedException, ExecutionException, RuntimeException {
		ScopedValue.where(NAME, "duke").run(() -> {
			try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
				 executor.submit(() -> {
					System.out.println(NAME.get());
				}).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Thread.currentThread().join();
	}
}
