package step4;

public class ScopeValueExample {
	private static final ScopedValue<String> NAME = ScopedValue.newInstance();
	public static void main(String[] args) throws InterruptedException {
		ScopedValue.where(NAME, "duke").run(() -> {
			// This works
			System.out.println(Thread.currentThread().getName() + ": " + NAME.get());
			try {
				Thread.ofVirtual().start(() -> {
//					try {
//						Thread.sleep(1000L);
//
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
						// This fails
						System.out.println(Thread.currentThread().getName() + ": " + NAME.get());
					}).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Thread.ofPlatform().start(() -> {
				// This fails
				System.out.println(Thread.currentThread().getName() + ": " + NAME.get());
			});
		});
		System.out.println("Outside ScopedValue scope");

		Thread.currentThread().join();
		
	}
}
