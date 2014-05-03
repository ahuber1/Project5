package hubble;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.imageio.ImageIO;

public class Processor implements Runnable {

	private Buffer buffer;
	private int t;
	private int n;
	private AtomicIntegerArray data;
	private byte[] bytes;
	private double sortTime;
	private String path;
	
	public Processor(Buffer buffer, int t, int n) {
		this.buffer = buffer;
		this.t = t;
		this.n = n;
	}
	
	@Override
	public void run() {
		try {
			data = buffer.toArray();
			
			long startTime = System.currentTimeMillis();
			
			if(buffer.size() > t)
				mergesort(0, buffer.size() - 1);
			else
				insertionsort(0, buffer.size() - 1);
			
			long diff = System.currentTimeMillis() - startTime;
			sortTime = diff / 1000.0;			
			
			bytes = new byte[data.length()];
			
			normalize();
			
			path = "images/output_N" + n + "_T" + t + ".jpg";
			File file = new File(path);
			InputStream in = new ByteArrayInputStream(bytes);
			BufferedImage image = ImageIO.read(in);
			ImageIO.write(image, "jpg", file);
		}
		catch (Exception e) {
			
		}
	}
	
	private void mergesort(int min, int max) {
		if(min == max)
			return;
		else
			insertionsort(min, max);
	}
	
	private void insertionsort(int min, int max) {
		for(int i = min; i < max - 1; i++) {
			for(int j = i + 1; j < max; j++) {
				if(data.get(i) > data.get(j)) {
					int temp = data.get(i);
					data.set(i, data.get(j));
					data.set(j, temp);
				}
			}
		}
	}
	
	private void normalize() {
		final int A = Collector.A;
		final int B = Collector.B;
		final int a = Byte.MIN_VALUE; 
		final int b = Byte.MAX_VALUE;
		
		for(int i = 0; i < bytes.length; i++) {
			int x = data.get(i);
			int v = a + (((x - A) * (b - a))/(B - a));
			bytes[i] = (byte) v;
		}
	}

	public double time() {
		return sortTime;
	}

	public String path() {
		return path;
	}
}
