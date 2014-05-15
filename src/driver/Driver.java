package driver;

import java.text.DecimalFormat;

import hubble.Buffer;
import hubble.Collector;
import hubble.Reciever;

public class Driver {
	
	public static void main(String[] args) {
		
		DecimalFormat format = new DecimalFormat("#,###");
		
		System.out.printf("Available processors (cores): %d\n", 
				Runtime.getRuntime().availableProcessors());
		
		System.out.printf("Available memory (bytes): %s\n", 
				format.format(Runtime.getRuntime().freeMemory()));
		
		System.out.println();
		
		try {
			// All the possible values for i
			int[] i = {8, 9, 10, 11};
			
			// All the possible values for j
			int[] j = {1, 2, 3, 4, 5};
			
			for(int x = 0, run = 1; x < i.length; x++) {
				int n = (int) Math.pow(2, i[x]);
				Buffer b1 = new Buffer(n * n * 2);
				Collector collector = new Collector(b1);
				Thread collectorThread = new Thread(collector);
				collectorThread.start();
				
				for(int y = 0; y < j.length; y++, run++) {
					int t = (int) Math.pow(10, j[y]);
					Buffer b2 = new Buffer(n * n);
					Reciever rec = new Reciever(b1, b2, n, t);
					Thread recThread = new Thread(rec);
					
					recThread.start();
					recThread.join();
					
					System.out.printf("Run #%d: i=%d,j=%d,N=%d,B1=%d,B2=%d,T=%d\n", 
							run, i[x], j[y], n, b1.num(), b2.num(), t);
					System.out.printf("Time mergesort: %dms\n", rec.mergesortTime());
					System.out.printf("Saving image: %s\n", rec.getFileName());
					System.out.println();
				}
				
				collector.stop();
				collectorThread.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
