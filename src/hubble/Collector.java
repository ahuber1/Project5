package hubble;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

// TODO see if we should rename this to "Satellite"

/**
 * This generates random numbers and stores them in a buffer, which simulates
 * data transmission with the Hubble Space Telescope
 * @author Andrew Huber
 *
 */
public class Collector implements Callable<String> {
	
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
	
	private ArrayList<Buffer> buffers;
	
	
	/** Keeps track of when the data generation process should stop */
	private AtomicBoolean stop;
	
	/**
	 * Creates a new collector that will generate random numbers and store them in a buffer, simulating
	 * the data transmission process found on the Hubble Space Telescope.
	 */
	public Collector() {
		this.stop = new AtomicBoolean(false);
		this.buffers = new ArrayList<Buffer>();
	}
	
	/**
	 * Adds a buffer that this collector will put data into
	 * @param buffer The buffer to add
	 */
	public void addBuffer(Buffer buffer) {
		buffers.add(buffer);
	}
	
	/**
	 * Stops the data generation process
	 */
	public void stop() {
		stop.set(true);
	}

	@Override
	public String call() throws Exception {
		Random generator = new Random();
		
		while(stop.get() == false) {
			//int delay = generator.nextInt(190 + 1) + 10;
			int value = generator.nextInt(B + 1);
			//Thread.sleep(delay);
			
			for(Buffer b: buffers) {
				b.add(value);
			}
		}
		
		return "Finished collecting data...";
	}
}
