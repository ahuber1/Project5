package hubble;

public class Handler implements Runnable {
	
	private Buffer b1;
	private Buffer b2;
	private Collector collector;
	private Reciever reciever;
	private Processor processor;
	private int runNum;
	private int i;
	private int j;
	private int n;
	private int t;
	
	public Handler(int runNum, int i, int j) {
		this.runNum = runNum;
		this.i = i;
		this.j = j;
		this.n = (int) Math.pow(2, i);
		this.t = (int) Math.pow(10, j);
	}
	
	@Override
	public void run() {
		try {
			b1 = new Buffer(n, launchReciever);
			collector = new Collector(n, b1);
			
			Thread thread = new Thread(collector);
			
			thread.start();
			thread.join();
			System.out.println(this.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		String line1 = String.format("Run #%d: i=%d,j=%d,N=%d,B1=%s,B2=%s,T=%d", runNum, i, j, n, b1, b2, t);
		String line2 = String.format("Time mergesort: %1.1fsec", processor.time());
		String line3 = String.format("Saving image: %s", processor.path());
		return String.format("%s\n%s\n%s\n", line1, line2, line3);
	}
	
	private Runnable launchReciever = new Runnable() {
		
		@Override
		public void run() {
			try {
				b2 = new Buffer(n, launchProcessor);
				reciever = new Reciever(b1, b2);
				
				Thread thread = new Thread(reciever);
				
				thread.start();
				thread.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
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
