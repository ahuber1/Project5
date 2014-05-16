package hubble;

import java.util.Random;

/**
 * This generates random numbers and stores them in a buffer, which simulates
 * data transmission with the Hubble Space Telescope
 * @author Andrew Huber
 *
 */
public class Satellite implements Runnable {
	
	/** The Buffer B1 as mentioned in the project document */
	private IntegerBuffer b1;	
	
	/** Keeps track of when the data generation process should stop */
	private boolean stop;
	
	/**
	 * Creates a new collector that will generate random numbers and store them in a buffer, simulating
	 * the data transmission process found on the Hubble Space Telescope.
	 */
	public Satellite(IntegerBuffer b1) {
		this.stop = false;
		this.b1 = b1;
	}
	
	/**
	 * Stops the data generation process
	 */
	public void stop() {
		stop = true;
	}

	/**
	 * Begins generating random integers between 0 and 4096
	 * and places them in the Buffer
	 */
	@Override
	public void run() {
		try {
			Random generator = new Random();
						
			while(stop == false) {
				int value = generator.nextInt(4096 + 1);
			
				while(b1.add(value) == false && stop == false)
					Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
