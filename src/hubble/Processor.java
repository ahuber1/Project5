package hubble;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.imageio.ImageIO;

// TODO "T" is used to determine when to stop splitting the array in mergesort; perform mergesort ALWAYS

/**
 * Processes the data that a {@link Reciever} recieved
 * @author Andrew Huber
 *
 */
public class Processor implements Runnable {

	/** The second buffer, B2, as specified in the project document */
	private Buffer b2;
	
	/** The value T, as specified in the project document */
	private int t;
	
	/** The value N, as specified in the project document */
	private int n;
	
	/** The data in B2 as an {@link AtomicIntegerArray} */
	private AtomicIntegerArray data;
	
	/** Will contain the normalized data */
	private byte[] bytes;
	
	/** Will store the amount of time in seconds it took to sort the data */
	private double sortTime;
	
	/** Will contain the file path of where the image is stored */
	private String path;
	
	/**
	 * Creates an object that will process the data that a {@link Reciever} recieved 
	 * @param b2 the second buffer, B2, as specified in the project document
	 * @param t the value T as specified in the project document
	 * @param n the value N as specified in the project document
	 */
	public Processor(Buffer b2, int t, int n) {
		this.b2 = b2;
		this.t = t;
		this.n = n;
	}
	
	/**
	 * Starts processing the data from the buffer. It performs the following steps:
	 * 
	 * <ol>
	 * <li>Sorts the data using merge sort or insertion sort (depending on the value of T)</li>
	 * <li>Normalizes the sorted data</li>
	 * <li>Generates a grayscale image using the normalized data</li>
	 * </ol>
	 */
	@Override
	public void run() {
		try {
			data = b2.toArray();
			
			long startTime = System.currentTimeMillis();
			
			if(b2.size() > t)
				mergesort(0, b2.size() - 1);
			else
				insertionsort(0, b2.size() - 1);
			
			long diff = System.currentTimeMillis() - startTime;
			sortTime = diff / 1000.0;			
			
			bytes = new byte[data.length()];
			
			normalize();
			
			File file = new File("images");
			
			if(file.exists() == false)
				file.mkdir();			
			
			path = String.format("images/output_N%d_T%d.jpg", n, t);
			file = new File(path);
			
			if(file.exists() == false)
				file.createNewFile();
			
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
			ImageIO.write(image, "jpg", file);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Performs merge sort on {@link Processor#data} between {@code min} and 
	 * {@code max}
	 * @param min an index number serving as the lower bounds
	 * @param max an index number serving as the upper bounds
	 */
	private void mergesort(int min, int max) {		
		final int MIN = min;
		final int MAX = max;
		final int MID = (MIN + MAX) / 2;
		
		if(min == max) {
			return;
		}
		else {
			Thread left = new Thread(new Runnable() {
				
				@Override
				public void run() {
					mergesort(MIN, MID);
				}
			}, String.format("mergesort(%d, %d) -> n=%d t=%d", MIN, MID, n, t));
			
			Thread right = new Thread(new Runnable() {
				
				@Override
				public void run() {
					mergesort(MID + 1, MAX);
				}
			}, String.format("mergesort(%d, %d) -> n=%d t=%d", MIN, MID + 1, n, t));
			
			try {
				left.start();
				left.join();
				right.start();
				right.join();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			insertionsort(min, max);
		}
	}
	
	/**
	 * Performs insertion sort on {@link Processor#data} between {@code min} and
	 * @param min an index number serving as the lower bounds
	 * @param max an index number serving as the upper bounds
	 */
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
	
	/**
	 * Performs the data normalization. This algorithm is based
	 * on what I learned from 
	 * <a href="http://www.howcast.com/videos/359111-How-to-Normalize-Data">this</a> 
	 * video
	 */
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
	
	/**
	 * Returns the amount of time it took to sort the data in seconds
	 * @return the amount of time it took to sort the data in seconds
	 */
	public double time() {
		return sortTime;
	}
	
	/**
	 * Returns the relative path of where the saved image is stored
	 * @return the relative path of where the saved image is stored
	 */
	public String path() {
		return path;
	}
}
