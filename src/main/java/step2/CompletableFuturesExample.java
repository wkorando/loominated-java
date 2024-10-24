package step2;

import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.VirtualThreadTaskExecutor;

public class CompletableFuturesExample {

	public static void main(String... args) throws Exception {
//		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new CompletableFuturesExample();
		String results = instance.callWebServices();
		System.out.println(results);
	}

	private String callWebServices() throws Exception {
		CompletableFuture<Void> cf = new CompletableFuture<>();
		cf.runAsync(() -> System.out.println( Thread.currentThread().isVirtual()), new VirtualThreadTaskExecutor());
		return "";
	}
}