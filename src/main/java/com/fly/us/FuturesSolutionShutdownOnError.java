package com.fly.us;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FuturesSolutionShutdownOnError {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to continue.");

		var instance = new FuturesSolutionShutdownOnError();
		String result = instance.callWebServices();
		System.out.println(result);
		WebServiceHelper.waitForUser("Press enter to exit.");
	}
	
	private String callWebServices() throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			var cf1 = WebServiceHelper.returnCf("success-jet", executor);
			var cf2 = WebServiceHelper.returnCf("http-ok-airways", executor);
			var cf3 = WebServiceHelper.returnCf("two-hundred-airlines", executor);
			var cf4 = WebServiceHelper.returnCf("not-found-air", executor);
//			var cf4 = WebServiceHelper.returnCf("success-jet", executor);

			var cfs = Stream.<CompletableFuture<String>>of(cf1, cf2, cf3, cf4);
			
			CompletableFuture.allOf(cf1, cf2, cf3, cf4);
			return cfs.map(f -> {
				try {
					return f.join();
				} catch (Exception e) {
					System.err.println("Exception occurred: " + e.getMessage());
					executor.shutdownNow();
					throw new RuntimeException(e.getMessage(), e);
				}
			}).collect(Collectors.joining(", ", "{ ", " }"));
			
//			Option 2			
//			CompletableFuture<String> allOfFuture = CompletableFuture.allOf(cf1, cf2, cf3, cf4)
//					.thenApply(_ -> cf1.join() + "," + cf2.join() + "," + cf3.join() + "," + cf4.join())
//					.exceptionally(err -> {
//						cf1.cancel(true);
//						cf2.cancel(true);
//						cf3.cancel(true);
//						cf4.cancel(true);
//						System.err.println("Exception occurred: " + err.getMessage());
//						return "a problem!";
//					});
//			allOfFuture.thenRun(() -> {
//				try {
//					List<String> results = cfs.map(f -> f.resultNow()).toList();
//					results.forEach(System.out::println);
//				} catch (Exception e) {
//					System.err.println("Exception occurred: " + e);
//				}
//			}).join();
//			
//			return "";

//			Option 3						
//			CompletableFuture.runAsync(() -> {
//					cfs.map(f -> {
//					try {
//						return f.join();
//					} catch (Exception e) {
//						System.err.println("Exception occurred: " + e.getMessage());
//						executor.shutdownNow();
//						throw new RuntimeException(e.getMessage(), e);
//					}
//				}).collect(Collectors.joining(", ", "{ ", " }"));
//
//			}, executor);
//			return result;
		} catch (Exception e) {

			throw e;

		}
	}
}