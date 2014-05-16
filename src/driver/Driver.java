package driver;

import java.text.DecimalFormat;

import hubble.IntegerBuffer;
import hubble.Satellite;
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
			
			// For every possible i
			for(int iIndex = 0, runNum = 1; iIndex < i.length; iIndex++) {
				int n = (int) Math.pow(2, i[iIndex]);
				IntegerBuffer b1 = new IntegerBuffer(n * n * 2);
				Satellite satellite = new Satellite(b1);
				Thread satelliteThread = new Thread(satellite);
				
				satelliteThread.start();
				
				for(int jIndex = 0; jIndex < j.length; jIndex++, runNum++) {
					int t = (int) Math.pow(10, j[jIndex]);
					IntegerBuffer b2 = new IntegerBuffer(n * n);
					Reciever rec = new Reciever(b1, b2, n, t);
					Thread recThread = new Thread(rec);
					
					recThread.start(); // start the receiving & processing
					recThread.join(); // wait for the receiving & processing to finish
					
					System.out.printf("Run #%d: i=%d,j=%d,N=%d,B1=%d,B2=%d,T=%d\n", 
							runNum, i[iIndex], j[jIndex], n, b1.size(), b2.size(), t);
					
					System.out.printf("Time mergesort: %dms\n", rec.getMergesortTime());
					
					System.out.printf("Saving image: %s\n", rec.getRelativeFilePath());
					
					System.out.println();
				}
				
				satellite.stop(); // "tell" the satellite to stop collecting data
				satelliteThread.join(); // wait for the satellite thread to finish
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
