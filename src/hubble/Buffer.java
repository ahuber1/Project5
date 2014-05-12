package hubble;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * A buffer that can be used to transfer {@code int} data
 * @author Andrew Huber
 *
 */
public class Buffer {
	
	private int[] buffer;
	private int head;
	private int tail;
	private int num;
	
	/**
	 * Creates a new Buffer of a particular size
	 * @param size the size of the buffer
	 */
	public Buffer(int size) {
		buffer = new int[size];
		head = 0;
		tail = 0;
		num = 0;
	}
	
	/**
	 * Add an {@code int} to the buffer
	 * @param i the {@code int} to add
	 * @return {@code true} if {@code i} was successfully added to the buffer because there was room
	 * for it, {@code false} if {@code i} was unsuccessfully added to the buffer because there was no room.
	 */
	public synchronized boolean add(int i) {
		if(num == buffer.length)
			return false;
		else {
			buffer[tail] = i;
			tail = increment(tail);
			num++;
			return true;
		}
	}
	
	private int increment(int val) {
		if(val + 1 == buffer.length)
			return 0;
		else
			return val + 1;
	}
	
	/**
	 * Removes an {@code int} from the head of the buffer
	 * @return The {@code int} from the head of the buffer or -1 if there is nothing to remove
	 */
	public synchronized int remove() {
		if(num == 0)
			throw new RuntimeException("Nothing to remove!");
		
		int val = buffer[head];
		head = increment(head);
		num--;
		return val;
	}
	
	/**
	 * Returns the number of items in this buffer
	 * @return the number of items in this buffer
	 */
	public synchronized int num() {
		return num;
	}
	
	/**
	 * Returns an {@link AtomicIntegerArray} of all the numbers stored in the buffer
	 * at a particular moment
	 * @return an {@link AtomicIntegerArray} of all the numbers stored in the buffer
	 * at a particular moment
	 */
	public synchronized int[] toArray() {
		int[] arr = new int[num];
		
		for(int i = 0, h = head; i < arr.length; i++, h = increment(h))
			arr[i] = buffer[h];
		
		return arr;
	}
	
	@Override
	public synchronized String toString() {
		return "TBD";
	}

	public synchronized int length() {
		return buffer.length;
	}
}
