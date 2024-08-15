package com.fly.us;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class WebServiceHelper {
	final static String BASE_URL = "http://localhost:8080/";

	public static String callWebService(String endpoint)
			throws IOException, InterruptedException, WebServiceException {
		System.out.println("Calling: " + endpoint);
		try (HttpClient httpClient = HttpClient.newHttpClient()) {
			HttpResponse<String> response = httpClient.send(
					HttpRequest.newBuilder(URI.create(BASE_URL + endpoint)).build(), BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				String errorMessage = "An error occurred while attempting to call endpoint: %s. Status code of: %d returned!";
				System.out.println(errorMessage.format(errorMessage, endpoint, response.statusCode()));
				throw new WebServiceException(errorMessage.format(errorMessage, endpoint, response.statusCode()),
						response.statusCode());
			}
			System.out.println("Response from calling " + endpoint + ", " + response.body());
			return response.body();
		}
	}
	
	public static CompletableFuture<String> returnCf(String endpoint, 
			ExecutorService executorService){
		return CompletableFuture.supplyAsync(() -> {
            try {
            	return WebServiceHelper.callWebService(endpoint);
            } catch (Exception e) {
            	executorService.shutdownNow();
                throw new RuntimeException(e);
            }
        }, executorService);
	}
	
	public static void waitForUser(String message) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(message);
		reader.readLine();
	}

}
