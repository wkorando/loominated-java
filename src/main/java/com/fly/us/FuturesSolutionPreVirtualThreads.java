package com.fly.us;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.stream.Collectors;

public class FuturesSolutionPreVirtualThreads {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to continue.");

		var instance = new FuturesSolutionPreVirtualThreads();
		String results = instance.callWebServices();
		System.out.println(results);

		WebServiceHelper.waitForUser("Press enter to exit.");
	}
	
	private String callWebServices() throws Exception {
		List<Future<String>> responses = new ArrayList<>();
		try (var executor = Executors.newFixedThreadPool(4)) {//What we'd use before loom
//		try (var executor = Executors.newScheduledThreadPool(4)) {
//		try (var executor = Executors.newCachedThreadPool()) {
			responses.add(executor.submit(() -> WebServiceHelper.callWebService("success-jet")));
			responses.add(executor.submit(() -> WebServiceHelper.callWebService("http-ok-airways")));
			responses.add(executor.submit(() -> WebServiceHelper.callWebService("two-hundred-airlines")));
		}

		return responses.stream().filter(f -> f.state() == State.SUCCESS).map(f -> f.resultNow())
				.collect(Collectors.joining(", ", "{ ", " }"));
	}
	
}