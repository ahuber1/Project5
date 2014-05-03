package hubble;

public class Reciever implements Runnable {
	
	private Buffer inputBuffer;
	private Buffer outputBuffer;
	
	public Reciever(Buffer inputBuffer, Buffer outputBuffer) {
		this.inputBuffer = inputBuffer;
		this.outputBuffer = outputBuffer;
	}
	
	
	@Override
	public void run() {
		int val = inputBuffer.remove();
		
		while(val != -1) {
			outputBuffer.add(val);
			val = inputBuffer.remove();
		}
	}
}
