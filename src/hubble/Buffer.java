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
	/** The actual buffer */
	private ConcurrentLinkedQueue<Integer> buffer;
	
	/** The maximum size of this buffer */
	private AtomicInteger size;
	
	/**
	 * Creates a new Buffer of a particular size
	 * @param size the size of the buffer
	 */
	public Buffer(int size) {
		this.size = new AtomicInteger(size);
		this.buffer = new ConcurrentLinkedQueue<Integer>();
	}
	
	/**
	 * Add an {@code int} to the buffer
	 * @param i the {@code int} to add
	 * @return {@code true} if {@code i} was successfully added to the buffer because there was room
	 * for it, {@code false} if {@code i} was unsuccessfully added to the buffer because there was no room.
	 */
	public synchronized boolean add(int i) {
		if(buffer.size() == size.get())
			return false;
		else {
			buffer.add(i);
			return true;
		}
	}
	
	/**
	 * Removes an {@code int} from the head of the buffer
	 * @return The {@code int} from the head of the buffer or -1 if there is nothing to remove
	 */
	public synchronized int remove() {
		
		Integer val = buffer.remove();
		
		if(val != null)
			return val;
		else
			return -1;
	}
	
	/**
	 * Returns the number of items in this buffer
	 * @return the number of items in this buffer
	 */
	public synchronized int size() {
		return buffer.size();
	}
	
	/**
	 * Returns an {@link AtomicIntegerArray} of all the numbers stored in the buffer
	 * at a particular moment
	 * @return an {@link AtomicIntegerArray} of all the numbers stored in the buffer
	 * at a particular moment
	 */
	public synchronized AtomicIntegerArray toArray() {
		Integer[] intArr = buffer.toArray(new Integer[buffer.size()]);
		AtomicIntegerArray atomArr = new AtomicIntegerArray(intArr.length);
		
		for(int i = 0; i < intArr.length; i++) {
			 atomArr.set(i, intArr[i].intValue());
		}
		
		return atomArr;
	}
	
	@Override
	public synchronized String toString() {
		return "TBD";
	}

	public int length() {
		return size.get();
	}
}
