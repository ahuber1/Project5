package driver;

import hubble.Handler;

public class Driver {
	
	public static void main(String[] args) {
		int[] i = {8, 9, 10, 11};
		int[] j = {1, 2, 3, 4, 5};
		Handler[] handlers = new Handler[i.length * j.length];
		Thread[] threads = new Thread[i.length * j.length];
		int indexVal = 0;
		
		for(int index = 0; index < i.length; index++) {
			for(int index2 = 0; index2 < j.length; index2++) {
				handlers[indexVal] = new Handler(indexVal + 1, i[index], j[index2]);
				threads[indexVal] = new Thread(handlers[indexVal]);
				threads[indexVal].start();
				indexVal++;
			}
		}
		
		for(int index = 0; index < threads.length; index++) {
			try {
				threads[index].join();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
