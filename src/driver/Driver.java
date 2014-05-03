package driver;

import hubble.Handler;

public class Driver {
	
	public static void main(String[] args) {
		int[] i = {8, 8, 8, 11, 11};
		int[] j = {1, 2, 3, 4, 5};
		Handler[] handlers = new Handler[i.length];
		Thread[] threads = new Thread[i.length];
		
		for(int index = 0; index < handlers.length; index++) {
			handlers[index] = new Handler(index + 1, i[index], j[index]);
			threads[index] = new Thread(handlers[index]);
			threads[index].start();
		}
		
		for(int index = 0; index < threads.length; index++) {
			try {
				threads[index].join();
			}
			catch (Exception e) {
				
			}
		}
	}
}
