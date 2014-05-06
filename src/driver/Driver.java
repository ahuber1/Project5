package driver;

import hubble.Buffer;
import hubble.Collector;
import hubble.Reciever;

public class Driver {
	
	public static void main(String[] args) {
		// All the possible values for i
		int[] i = {8, 9, 10, 11};
		
		// All the possible values for j
		int[] j = {1, 2, 3, 4, 5};
		
		Thread[] threads = new Thread[i.length * j.length + 1];
 		Collector collector = new Collector();
 		threads[0] = new Thread(collector);
 		int numThreads = 1;
 		
 		for(int x = 0; x < i.length; x++) {
 			int n = (int) Math.pow(2, i[x]);
 			
 			for(int y = 0; y < j.length; y++) {
 				int t = (int) Math.pow(10, j[y]);
 				Buffer b1 = new Buffer(n * n * 2);
 				Buffer b2 = new Buffer(n * n);
 				Reciever r = new Reciever(b1, b2, n, t);
 				
 				collector.addBuffer(b1);
 				threads[numThreads] = new Thread(r);
 				numThreads++;
 			}
 		}
 		
 		for(int x = 0; x < threads.length; x++)
 			threads[x].start();
 		
 		try {
 			for(int x = 1; x < threads.length; x++)
 	 			threads[x].join();
 			
 			collector.stop();
 			threads[0].join();
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 		
	}
}
