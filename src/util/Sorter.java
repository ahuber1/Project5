package util;

import hubble.Processor;

/**
 * Provides a way to sort integers using Merge Sort
 * @author andrew_huber
 *
 */
public class Sorter {
	
	/**
	 * Sorts integers using merge sort
	 * @param array the array to sort
	 * @param t when the size of a subarray, <i>s</i> <=  <i>t</i>, 
	 * insertion sort is used
	 * @return the sorted array
	 */
	public static int[] mergesort(int[] array, int t) {
		mergesort(array, t, 0, array.length - 1);
		return array;
	}
	
	/**
	 * Sorts integers using merge sort. Masks the recursive implementation
	 * of {@link Sorter#mergesort(int[], int)}
	 * @param array the array to sort
	 * @param t when the size of a subarray, <i>s</i> <=  <i>t</i>, 
	 * insertion sort is used
	 * @param min the starting index of the "subarray" being analyzed
	 * @param max the ending index of the "subarray" being analyzed
	 */
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
	
	/**
	 * Merges two "subarrays" together
	 * @param array the array to "merge" 
	 * @param min the smallest index of the "left subarray"
	 * @param mid the largest index of the "left subarray"
	 * @param max the largest index of the "right subarray"
	 */
	private  static void merge(int[] array, int min, int mid, int max) {
		
		// the sorted copy of this "subarray"
		int[] a = new int[max - min + 1];
		
		for(int i = 0, l = min, r = mid + 1; i < a.length; i++, l++, r++) {
			
			if(l == mid + 1) { // If all the elements in the left "subarray" have been processed
				a[i] = array[r];
				l--; // stops l from being incremented
			}
			else if(r == max + 1) { // If all the elements in the "right" subarray have been processed
				a[i] = array[l];
				r--; // stops r from being incremented
			}
			else if(array[l] < array[r]) { 
				a[i] = array[l];
				r--; // stops r from being incremented
			}
			else {
				a[i] = array[r];
				l--; // stops l from being incremented
			}
			
		}
		
		// copy the contents of the copy array back into the original
		for(int i = 0, m = min; i < a.length; i++, m++)
			array[m] = a[i];
	}

	/**
	 * Performs insertion sort on {@link Processor#data} between {@code min} and
	 * @param min an index number serving as the smallest index of this "subarray"
	 * @param max an index number serving as the largest index of this "subarray"
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
	
	/**
	 * Used by {@link Sorter#mergesort(int[], int, int, int)} to perform the threaded
	 * version of Merge Sort
	 * @author Andrew
	 *
	 */
	private static class SortTask implements Runnable {
		/** the array to sort */
		private int[] array;
		
		/** T as described in the project description */
		private int t;
		
		/** An index number serving as the smallest index of this "subarray" */
		private int min;
		
		/** An index number serving as the largest index of this "subarray" */
		private int max;
		
		/**
		 * Creates a new {@code SortTask}
		 * @param array the array to sort
		 * @param t when the size of a subarray, <i>s</i> <=  <i>t</i>, 
		 * insertion sort is used
		 * @param min the starting index of the "subarray" being analyzed
		 * @param max the ending index of the "subarray" being analyzed
		 */
		public SortTask(int[] array, int t, int min, int max) {
			this.array = array;
			this.t = t;
			this.min = min;
			this.max = max;
		}
		
		/**
		 * Performs a merge sort on this "subarray"
		 */
		@Override
		public void run() {
			Sorter.mergesort(array, t, min, max);
		}		
	}
}
