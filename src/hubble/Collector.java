package hubble;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Collector implements Runnable {

	public static final int A = 0;
	public static final int B = 4096;
	
	private Buffer buffer;
	private AtomicBoolean stop;
	
	public Collector(int n, Buffer buffer) {
		this.buffer = buffer;
		this.stop = new AtomicBoolean(false);
	}
	
	public void stop() {
		stop.set(true);
	}
	
	@Override
	public void run() {
		try {
			Random generator = new Random();
			
			while(stop.get() == false) {
				int delay = generator.nextInt(190) + 10 + 1;
				int value = generator.nextInt(B + 1);
				Thread.sleep(delay);
				buffer.add(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
