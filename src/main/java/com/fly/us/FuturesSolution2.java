package com.fly.us;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.stream.Collectors;

public class FuturesSolution2 {

	public static void main(String... args) throws IOException {
		WebServiceHelper.waitForUser("Press enter to continue.");

		List<Future<String>> responses = new ArrayList<>();
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			responses.add(executor.submit(() -> WebServiceHelper.callWebService("success-jet")));
			responses.add(executor.submit(() -> WebServiceHelper.callWebService("http-ok-airways")));
			responses.add(executor.submit(() -> WebServiceHelper.callWebService("two-hundred-airlines")));
			responses.add(executor.submit(() -> WebServiceHelper.callWebService("not-found-air")));

		}

		String results = responses.stream().filter(f -> f.state() == State.SUCCESS).map(f -> f.resultNow())
				.collect(Collectors.joining(", ", "{ ", " }"));

		System.out.println(results);

		WebServiceHelper.waitForUser("Press enter to exit.");
	}
}