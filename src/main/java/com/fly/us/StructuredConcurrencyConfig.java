package com.fly.us;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructuredConcurrencyConfig {

	public static void main(String... args) throws Throwable {

//		WebServiceHelper.continueExecuting("Press enter to continue.");
		System.out.println("Thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual()  );
		StructuredConcurrencyConfig instance = new StructuredConcurrencyConfig();
		instance.callWebServices();

//		WebServiceHelper.continueExecuting("Press enter to exit.");
	}

	private void callWebServices() throws Throwable {
		List<Subtask<String>> responses = new ArrayList<>();
		try (var scope = StructuredTaskScope.<String, Stream<Subtask<String>>>open(Joiner.allSuccessfulOrThrow()
				//, cf -> cf.withTimeout(Duration.ofMillis(3000L)
				)) {
//		try (var scope = StructuredTaskScope.open(Joiner.all())) {

		    scope.fork(() -> WebServiceHelper.callWebService("success-jet"));// 500ms
			scope.fork(() -> WebServiceHelper.callWebService("http-ok-airways"));// 500ms
			scope.fork(() -> WebServiceHelper.callWebService("two-hundred-airlines"));// 500ms
			scope.fork(() -> WebServiceHelper.callWebService("delayed-travel"));// 5000ms
			scope.fork(() -> WebServiceHelper.callWebService("not-found-air"));// 100ms
			var result = scope.join();//Map this to the return type of subtask
			String results = result
					.map(f -> f.get())
					.collect(Collectors.joining(", ", "{ ", " }"));
			System.out.println(results);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String results = responses.stream()
				.filter(f -> f.state() == State.SUCCESS)
				.map(f -> f.get())
				.collect(Collectors.joining(", ", "{ ", " }"));
		System.out.println(results);
		//Do a return here instead of sout
	}

}
