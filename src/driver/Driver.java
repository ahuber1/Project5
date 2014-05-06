package driver;

import hubble.Buffer;
import hubble.Collector;
import hubble.Reciever;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Driver {
	
	public static void main(String[] args) {
		// All the possible values for i
		int[] i = {8, 9, 10, 11};
		
		// All the possible values for j
		int[] j = {1, 2, 3, 4, 5};
		
		ExecutorService service = new ForkJoinPool();
		Set<Callable<String>> callables = new HashSet<Callable<String>>();
		Collector collector = new Collector();
		
		callables.add(collector);
		
		for(int a = 0; a < i.length; a++) {
			
			int n = (int) Math.pow(2, i[a]);
			
			for(int b = 0; b < j.length; b++) {
				int t = (int) Math.pow(10, j[b]);
				Buffer b1 = new Buffer(n * n * 2);
				Buffer b2 = new Buffer(n * n);
				Reciever rec = new Reciever(b1, b2, n, t);
				
				collector.addBuffer(b1);
				callables.add(rec);
			}
		}
		
		try {
			service.invokeAll(callables);
			service.wait();
		} catch (Exception e) {
		 e.printStackTrace();
		}
	}
}
