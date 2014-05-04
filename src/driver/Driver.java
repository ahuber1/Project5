package driver;

import hubble.Handler;

public class Driver {
	
	public static void main(String[] args) {
		// All the possible values for i
		int[] i = {8, 9, 10, 11};
		
		// ALl the possible values for j
		int[] j = {1, 2, 3, 4, 5};
		
		// All of the Handlers that will be generated
		Handler[] handlers = new Handler[i.length * j.length];
		
		// All of the threads that will execute
		Thread[] threads = new Thread[i.length * j.length];
		
		// Used to place Handler and Thread objects into "handlers" and "threads", respectively
		int indexVal = 0;
		
		// For every possible i and j combination, create a handler with that combination, create a Thread
		// using that Handler, and run the handler.
		//
		// FOR TESTING PURPOSES, I AM ONLY RUNNING THE FIRST THREAD THIS LOOP GENERATES!!!
		for(int index = 0; index < i.length; index++) {
			for(int index2 = 0; index2 < j.length; index2++) {
				handlers[indexVal] = new Handler(indexVal + 1, i[index], j[index2]);
				threads[indexVal] = new Thread(handlers[indexVal], String.format("i=%d j=%d)", i[index], j[index2]));
				
				if(indexVal == 0)
					threads[indexVal].start();
				
				indexVal++;
			}
		}
		
		// Join all the threads
		//
		// FOR TESTING PURPOSES, I AM ONLY WAITING FOR THE FIRST THREAD TO DIE!!!
		for(int index = 0; index < threads.length; index++) {
			try {
				if(index == 0)
					threads[index].join();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
}
