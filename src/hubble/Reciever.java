package hubble;

/**
 * Receives input from a Buffer
 * @author Andrew Huber
 *
 */
public class Reciever implements Runnable {
	
	/** Buffer B1 as specified by the project document */
	private IntegerBuffer b1;
	
	/** Buffer B2 as specified by the project document */
	private IntegerBuffer b2;
	
	private Processor proc;
	
	/**
	 * Creates a receiver object that will receive input from a buffer
	 * @param b1 buffer B1 as specified by the project document
	 * @param b2 buffer B2 as specified by the project document
	 * @param n the value N as specified by the project document
	 */
	public Reciever(IntegerBuffer b1, IntegerBuffer b2, int n, int t) {
		this.b1 = b1;
		this.b2 = b2;
		proc = new Processor(b2, t, n);
	}
	
	/**
	 * Waits for the buffer to be half full and then adds those elements
	 * into another buffer, which is subsequently processed into images. 
	 */
	@Override
	public void run() {
		try {
			while(b1.size() < (b1.length() / 2)) {
				Thread.sleep(1000);
			}
			
			int val;
			boolean loop;
			
			do {
				val = b1.remove();
				loop = b2.add(val);
				
			} while(loop == true);
			
			proc.processData();
		} catch (Exception e) {
			System.err.println("My catch!");
			e.printStackTrace();
		}
	}

	/** 
	 * Returns the relative file path of the image that was generated
	 * @return the relative file path of the image that was generated
	 */
	public String getRelativeFilePath() {
		return proc.getRelativeFilePath();
	}
	
	/**
	 * Returns the amount of time it took to sort the image in milliseconds
	 * @return the amount of time it took to sort the image in milliseconds
	 */
	public long getMergesortTime() {
		return proc.getMergesortTime();
	}
}