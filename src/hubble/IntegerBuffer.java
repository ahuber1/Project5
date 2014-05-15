package hubble;

/**
 * A buffer that is used to store {@code int} data
 * @author Andrew Huber
 */
public class IntegerBuffer {
	
	/** The buffer is implemented as a circular queue; this is the circular queue */
	private volatile int[] buffer;
	
	/** The index in {@link IntegerBuffer#buffer} where the head of the queue lies */
	private volatile int head;
	
	/** The index in {@link IntegerBuffer#buffer} where the tail of the queue lies */
	private volatile int tail;
	
	/** The number of items stored in this queue */
	private volatile int num;
	
	/**
	 * Creates a new {@code IntegerBuffer} of a particular size
	 * @param size the size of the buffer
	 */
	public IntegerBuffer(int size) {
		buffer = new int[size];
		head = 0;
		tail = 0;
		num = 0;
	}
	
	/**
	 * Add an {@code int} to the buffer
	 * @param i the {@code int} to add
	 * @return {@code true} if {@code i} was successfully added to the buffer because there was 
	 * room for it, {@code false} if {@code i} was <u>un</u>successfully added to the buffer 
	 * because there was no room.
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
	
	/**
	 * Used to increment indices in a manner that will wrap them around the values if necessary. 
	 * For example, if this buffer is of size <i>l</i>, then accessing the buffer at the 
	 * <i>l</i><sup>&nbspth</sup> position will lead to an 
	 * {@link ArrayIndexOutOfBoundsException}. This method simply causes the indices to 
	 * wrap around. Therefore, in the aforementioned scenario, index 
	 * <i>i</i> = <i>l</i> - 1 will be incremented to 0 and not <i>l</i>.
	 * @param val The value to increment
	 * @return
	 */
	private int increment(int val) {
		if(val + 1 == buffer.length)
			return 0;
		else
			return val + 1;
	}
	
	/**
	 * Removes an {@code int} from the head of the buffer
	 * @return The {@code int} from the head of the buffer
	 * @throws RuntimeException if there is nothing to remove
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
	 * Returns an array of all the numbers stored in the buffer
	 * at a particular moment
	 * @return an array of all the numbers stored in the buffer
	 * at a particular moment
	 */
	public synchronized int[] toArray() {
		int[] arr = new int[num];
		
		for(int i = 0, h = head; i < arr.length; i++, h = increment(h))
			arr[i] = buffer[h];
		
		return arr;
	}

	/**
	 * Returns the length of this buffer (<i><u>NOT</u></i> the number of items stored in this
	 * buffer)
	 * @return the length of this buffer
	 */
	public synchronized int length() {
		return buffer.length;
	}

	/**
	 * Returns the amount of time it took to sort the data in seconds
	 * @param processor TODO
	 * @return the amount of time it took to sort the data in seconds
	 */
	public long mergesortTime(Processor processor) {
		return processor.sortTime;
	}
}