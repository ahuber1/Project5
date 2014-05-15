package hubble;

import java.util.Random;

// TODO see if we should rename this to "Satellite"

/**
 * This generates random numbers and stores them in a buffer, which simulates
 * data transmission with the Hubble Space Telescope
 * @author Andrew Huber
 *
 */
public class Satellite implements Runnable {
	
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
	public synchronized void stop() {
		stop = true;
	}

	@Override
	public void run() {
		try {
			Random generator = new Random();
						
			while(stop == false) {
				int value = generator.nextInt(4096 + 1);
			
				while(b1.add(value) == false)
					Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
