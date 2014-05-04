package hubble;

/**
 * Receives input from a Buffer
 * @author Andrew Huber
 *
 */
public class Reciever implements Runnable {
	
	/** Buffer B1 as specified by the project document */
	private Buffer b1;
	
	/** Buffer B2 as specified by the project document */
	private Buffer b2;
	
	/** The value N as specified by the project document */
	private int n;
	
	/**
	 * Creates a receiver object that will receive input from a buffer
	 * @param b1 buffer B1 as specified by the project document
	 * @param b2 buffer B2 as specified by the project document
	 * @param n the value N as specified by the project document
	 */
	public Reciever(Buffer b1, Buffer b2, int n) {
		this.b1 = b1;
		this.b2 = b2;
		this.n = n;
	}
	
	/**
	 * Begins receiving data from the buffer
	 */
	@Override
	public void run() {		
		int val = b1.remove();
		
		for(int i = 0; i < n * n; i++) {			
			b2.add(val);
			val = b1.remove();
		}
	}
}