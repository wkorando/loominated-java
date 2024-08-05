package com.fly.us;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

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
			var cf1 = WebServiceHelper.returnCf("success-jet", executor);
			var cf2 = WebServiceHelper.returnCf("http-ok-airways", executor);
			var cf3 = WebServiceHelper.returnCf("two-hundred-airlines", executor);

			return (String) CompletableFuture.anyOf(cf1, cf2, cf3).get();

		} catch (Exception e) {

			throw e;

		}
	}
}

//List<Future<String>> responses = new ArrayList<>();
//try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
//
//	responses.add(executor.submit(() -> WebServiceHelper.callWebService("success-jet")));
//	responses.add(executor.submit(() -> WebServiceHelper.callWebService("http-ok-airways")));
//	responses.add(executor.submit(() -> WebServiceHelper.callWebService("two-hundred-airlines")));
//
//	while (!responses.stream().filter(f -> f.state() == State.SUCCESS).findAny().isPresent()) {
//	}
//	executor.shutdownNow();
//}
//
//String results = responses.stream().filter(f -> f.state() == State.SUCCESS).map(f -> f.resultNow())
//		.collect(Collectors.joining(", ", "{ ", " }"));
