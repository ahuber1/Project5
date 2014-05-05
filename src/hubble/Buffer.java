package hubble;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * A buffer that can be used to transfer {@code int} data
 * @author Andrew Huber
 *
 */
public class Buffer {
	/** The actual buffer */
	private ConcurrentLinkedQueue<Integer> buffer;
	
	/** 
	 * {@code N} provided by {@code Driver.java}; look in project
	 * description doc for more information
	 */
	private int n;
	
	/** The maximum size of this buffer */
	private int size;
	
	/** A runnable that will be called when N<sup>2</sup> items have been added */
	private Runnable runnable;
	
	/** The {@linkplain Buffer#runnable runnable} will only be called once; this
	 *  boolean flag makes sure that it is only called once. 
	 */
	private boolean runnableCalled;
	
	/**
	 * Creates a new Buffer to store {@code int} values
	 * @param n provided by {@code Driver.java}; look in project description doc for more information
	 * @param runnable A runnable that will be called when N<sup>2</sup> items have been added; if you
	 * want no task to be performed at that time, pass in {@code null}
	 */
	 
	 // TODO Do not require "n"; replace with size
	public Buffer(int n, Runnable runnable) {
		this.n = n;
		this.size = n * n * 2;
		this.buffer = new ConcurrentLinkedQueue<Integer>();
		this.runnable = runnable;
		this.runnableCalled = false;
	}
	
	/**
	 * Add an {@code int} to the buffer
	 * @param i the {@code int} to add
	 * @return {@code true} if {@code i} was successfully added to the buffer because there was room
	 * for it, {@code false} if {@code i} was unsuccessfully added to the buffer because there was no room.
	 */
	public synchronized boolean add(int i) {
		if(buffer.size() == size)
			return false;
		
		buffer.add(i);
		
		if(runnable != null && buffer.size() == n * n && runnableCalled == false) {
			runnableCalled = true;
			runnable.run();
		}
		
		return true;
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
}
