package common;

import java.util.concurrent.TimeUnit;

public class CommonUtils {

	public String task(String value, long executionTime) throws InterruptedException {
		TimeUnit.MILLISECONDS.sleep(executionTime);
		String result = 
				String.format("""
				{
					"value" : %s
				}
				""", value);
		System.out.println(result);
		return result;
	}
}
