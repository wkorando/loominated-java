package step4;

import common.CommonUtils;

public class ScopedValuesNonSTSVirutalThreads {
	final static ScopedValue<String> NAME = ScopedValue.newInstance();

	public static void main(String... args) throws Throwable {

		CommonUtils.waitForUser("Press enter to continue.");
		ScopedValue.<String>where(NAME, "Hello World").run(() -> {

		 System.out.println(NAME.get());

		});
		ScopedValuesNonSTSVirutalThreads instance = new ScopedValuesNonSTSVirutalThreads();
//		String results = instance.callWebServices();
//		System.out.println(results);
		CommonUtils.waitForUser("Press enter to exit.");
	}

//	private String callWebServices() throws Throwable {
//		return ScopedValue.<String>where(NAME, "Hello World").call(() -> {
//
//			return NAME.get();
//
//		});
//	}

}
