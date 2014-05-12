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
	
	private Processor proc;
	
	/**
	 * Creates a receiver object that will receive input from a buffer
	 * @param b1 buffer B1 as specified by the project document
	 * @param b2 buffer B2 as specified by the project document
	 * @param n the value N as specified by the project document
	 */
	public Reciever(Buffer b1, Buffer b2, int n, int t) {
		this.b1 = b1;
		this.b2 = b2;
		proc = new Processor(b2, t, n);
	}
	
	@Override
	public void run() {
		try {
			while(b1.num() < (b1.length() / 2)) {
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
}