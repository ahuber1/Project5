package util;

import hubble.Processor;

import java.util.concurrent.Callable;

public class Sorter {
	
	public static int[] mergesort(int[] array, int t) {
		mergesort(array, t, 0, array.length - 1);
		return array;
	}
	
	private static void mergesort(int[] array, int t, int min, int max) {
		if(max - min <= t)
			insertionsort(array, min, max);
		else {			
			int mid = (min + max) / 2;
			SortTask left = new SortTask(array, t, min, mid);
			SortTask right = new SortTask(array, t, mid + 1, max);			
			Thread leftThread = new Thread(left);
			Thread rightThread = new Thread(right);
			
			leftThread.run();
			rightThread.run();
			
			while(true) {
				try {
					leftThread.join();
					break;
				} catch (InterruptedException e) {
					// try again
				}
			}
			
			while(true) {
				try {
					rightThread.join();
					break;
				} catch (InterruptedException e) {
					// try again
				}
			}
			
			merge(array, min, mid, max);
		}
	}
	
	private  static void merge(int[] array, int min, int mid, int max) {
		
		int[] a = new int[max - min + 1];
		
		for(int i = 0, l = min, r = mid + 1; i < a.length; i++, l++, r++) {
			
			if(l == mid + 1) {
				a[i] = array[r];
				l--;
			}
			else if(r == max + 1) {
				a[i] = array[l];
				r--;
			}
			else if(array[l] < array[r]) {
				a[i] = array[l];
				r--;
			}
			else {
				a[i] = array[r];
				l--;
			}
			
		}
		
		for(int i = 0, m = min; i < a.length; i++, m++)
			array[m] = a[i];
	}

	/**
	 * Performs insertion sort on {@link Processor#data} between {@code min} and
	 * @param min an index number serving as the lower bounds
	 * @param max an index number serving as the upper bounds
	 */
	private synchronized static void insertionsort(int[] array, int min, int max) {		
		for(int i = min; i <= max; i++) {
			for(int j = i; j <= max; j++) {
				if(array[j] < array[i]) {
					int temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
	}
	
	private static class SortTask implements Callable<String>, Runnable {
		private int[] array;
		private int t;
		private int min;
		private int max;
		
		public SortTask(int[] array, int t, int min, int max) {
			this.array = array;
			this.t = t;
			this.min = min;
			this.max = max;
		}
		
		@Override
		public void run() {
			call();
		}


		@Override
		public String call() {
			Sorter.mergesort(array, t, min, max);
			return "Done!";
		}
		
	}
}
