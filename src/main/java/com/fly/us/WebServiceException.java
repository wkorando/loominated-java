package com.fly.us;

class WebServiceException extends Exception {
	int statusCode;

	public WebServiceException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	public int statusCode() {
		return statusCode;
	}
}