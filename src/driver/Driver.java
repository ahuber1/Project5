package driver;

import hubble.Buffer;
import hubble.Collector;
import hubble.Reciever;

public class Driver {
	
	public static void main(String[] args) {
		try {
			// All the possible values for i
			int[] i = {8, 9, 10, 11};
			
			// All the possible values for j
			int[] j = {1, 2, 3, 4, 5};
			
			for(int x = 0; x < i.length; x++) {
				int n = (int) Math.pow(2, i[x]);
				Buffer b1 = new Buffer(n * n * 2);
				Collector collector = new Collector(b1);
				Thread collectorThread = new Thread(collector);
				collectorThread.start();
				
				for(int y = 0; y < j.length; y++) {
					int t = (int) Math.pow(10, j[y]);
					Reciever rec = new Reciever(b1, new Buffer(n * n), n, t);
					Thread recThread = new Thread(rec);
					
					System.out.printf("N = %d\tT = %d\tNumber of Threads: %d\n", n, t, Thread.activeCount());
					
					recThread.start();
					recThread.join();
				}
				
				collector.stop();
				collectorThread.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
