package hubble;

/**
 * Handles all processes for this hubble simulator for a given pair of i
 * and j
 * @author Andrew Huber
 *
 */
public class Handler implements Runnable {
	
	/** Buffer B1 as specified in the project document */
	private Buffer b1;
	
	/** Buffer B2 as specified in the project document */
	private Buffer b2;
	
	/** The hubble simulator simulating hubble's data-collection process */
	private Collector collector;
	
	/** An object representing a ground-station receiver of all this data */
	private Reciever receiver;
	
	/** 
	 * An object representing a ground-station processor that processes all the data
	 * from {@link Handler#receiver}
	 */
	private Processor processor;
	
	/**
	 *  The run number: used in {@link Handler#toString()} to format the output as specified
	 *  in the project document 
	 */
	private int runNum;
	
	/** i as specified in the project document */
	private int i;
	
	/** j as specified in the project document */
	private int j;
	
	/** N as specified in the project document */
	private int n;
	
	/** T as specified in the project document */
	private int t;
	
	/**
	 * Creates a Handler object that handles all processes for this hubble simulator for a 
	 * given pair of i and j
	 * @param runNum the run number: used in {@link Handler#toString()} to format the output as specified
	 *  in the project document 
	 * @param i the value i as specified in the project document
	 * @param j the value j as specified in the project document
	 */
	public Handler(int runNum, int i, int j) {
		this.runNum = runNum;
		this.i = i;
		this.j = j;
		this.n = (int) Math.pow(2, i);
		this.t = (int) Math.pow(10, j);
	}
	
	/**
	 * Starts the simulation
	 */
	@Override
	public void run() {		
		try {
			b1 = new Buffer(n, launchReciever);
			collector = new Collector(n, b1);
			
			Thread thread = new Thread(collector, String.format("Collector (i=%d j=%d)", i, j));
			
			thread.start();
			thread.join();
			System.out.println(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a string representation of this object; is used to make the output of this program
	 * look like the output specified in the project document
	 */
	@Override
	public String toString() {		
		String line1 = String.format("Run #%d: i=%d,j=%d,N=%d,B1=%s,B2=%s,T=%d", runNum, i, j, n, b1, b2, t);
		String line2 = String.format("Time mergesort: %1.1fsec", processor.time());
		String line3 = String.format("Saving image: %s", processor.path());
		return String.format("%s\n%s\n%s\n", line1, line2, line3);
	}
	
	/**
	 * Once the first buffer, B1, has N<sup>2</sup> items in it, this {@link Runnable} is executed in order
	 * to launch the {@link Reciever}, which populates buffer B2.
	 */
	private Runnable launchReciever = new Runnable() {
		
		@Override
		public void run() {						
			try {
				b2 = new Buffer(n, launchProcessor);
				receiver = new Reciever(b1, b2, n);
				
				Thread thread = new Thread(receiver, String.format("Reciever (i=%d j=%d)", i, j));
				
				thread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	/**
	 * Once the second buffer, B2, has N<sup>2</sup> items in it, this {@link Runnable} is executed in order
	 * to launch the {@link Processor}, which processes the data and makes the images.
	 */
	private Runnable launchProcessor = new Runnable() {
		
		@Override
		public void run() {			
			try {
				collector.stop();
				processor = new Processor(b2, t, n);
				processor.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
