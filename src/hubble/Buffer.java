package hubble;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Buffer {
	
	private ConcurrentLinkedQueue<Integer> buffer;
	private int n;
	private int length;
	private Runnable runnable;
	
	public Buffer(int n, Runnable runnable) {
		this.n = n;
		this.length = n * n * 2;
		this.buffer = new ConcurrentLinkedQueue<Integer>();
		this.runnable = runnable;
	}
	
	public synchronized boolean add(int i) {
		if(buffer.size() == length)
			return false;
		
		buffer.add(i);
		
		if(runnable != null && buffer.size() == n * n)
			runnable.run();
		
		return true;
	}
	
	public synchronized int remove() {
		Integer val = buffer.remove();
		
		if(val != null)
			return val;
		else
			return -1;
	}

	public synchronized int size() {
		return buffer.size();
	}
	
	public synchronized AtomicIntegerArray toArray() {
		Integer[] intArr = buffer.toArray(new Integer[buffer.size()]);
		AtomicIntegerArray atomArr = new AtomicIntegerArray(intArr.length);
		
		for(int i = 0; i < intArr.length; i++) {
			 atomArr.set(i, intArr[i].intValue());
		}
		
		return atomArr;
	}
	
	@Override
	public String toString() {
		return "TBD";
	}
}
