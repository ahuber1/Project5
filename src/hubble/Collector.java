package hubble;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This generates random numbers and stores them in a buffer, which simulates
 * data transmission with the Hubble Space Telescope
 * @author Andrew Huber
 *
 */
public class Collector implements Runnable {
	
	/**
	 * The smallest number that this Collector's random number generator can generate.
	 * This is used by {@link Processor} to normalize the data that is generated.
	 * The nomenclature of this variable follows a video tutorial on data normalization
	 * that I used as a basis for my data normalization algorithm. The aforementioned 
	 * video can be found <a href="http://www.howcast.com/videos/359111-How-to-Normalize-Data">here</a>.
	 */
	public static final int A = 0;
	
	/**
	 * The largest number that this Collector's random number generator can generate.
	 * This is used by {@link Processor} to normalize the data that is generated.
	 * The nomenclature of this variable follows a video tutorial on data normalization
	 * that I used as a basis for my data normalization algorithm. The aforementioned 
	 * video can be found <a href="http://www.howcast.com/videos/359111-How-to-Normalize-Data">here</a>.
	 */
	public static final int B = 4096;
	
	/** Buffer B1 as specified in the project document */
	private Buffer b1;
	
	/** Keeps track of when the data generation process should stop */
	private AtomicBoolean stop;
	
	/**
	 * Creates a new collector that will generate random numbers and store them in a buffer, simulating
	 * the data transmission process found on the Hubble Space Telescope.
	 * @param n the value N as described in the project document
	 * @param b1 the buffer B1 as described in the project document
	 */
	public Collector(int n, Buffer b1) {
		this.b1 = b1;
		this.stop = new AtomicBoolean(false);
	}
	
	/**
	 * Stops the data generation process
	 */
	public void stop() {
		stop.set(true);
	}
	
	/**
	 * Begins the data generation process
	 */
	@Override
	public void run() {
		try {
			Random generator = new Random();
			
			while(stop.get() == false) {
				//int delay = generator.nextInt(190) + 10 + 1;
				int value = generator.nextInt(B + 1);
				//Thread.sleep(delay);
				b1.add(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
