package hubble;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.imageio.ImageIO;

import util.Sorter;

/**
 * Processes the data that a {@link Reciever} recieved
 * @author Andrew Huber
 *
 */
public class Processor {

	/** The second buffer, B2, as specified in the project document */
	private Buffer b2;
	
	/** The value T, as specified in the project document */
	private int t;
	
	/** The value N, as specified in the project document */
	private int n;
	
	/** The data in B2 as an {@link AtomicIntegerArray} */
	private int[] data;
	
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
	public void processData() {
		try {		
			System.out.println("Starting sort...");
			
			long startTime = System.currentTimeMillis();
			data = Sorter.mergesort(b2.toArray(), t);
			long diff = System.currentTimeMillis() - startTime;
			
			System.out.printf("It took %d seconds to sort\n", diff);			
			
			sortTime = diff / 1000.0;			
			bytes = new byte[data.length];
			
			System.out.println("Starting normalization...");
			normalize();
			System.out.println("Done normalization!");
			
			File file = new File("images");
			
			if(file.exists() == false)
				file.mkdir();
			
			path = String.format("images/output_N%d_T%d.jpg", n, t);
			file = new File(path);
			
			if(file.exists())
				file.delete();
			
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			BufferedImage image = new BufferedImage((int) Math.sqrt(bytes.length),
					(int) Math.sqrt(bytes.length), BufferedImage.TYPE_BYTE_GRAY);
			WritableRaster raster = image.getRaster();
					
			for(int i = 0; i < image.getHeight(); i++) {
				for(int j = 0; j < image.getHeight(); j++) {
					raster.setPixel(i, j, new double[] {bais.read()});
				}
			}
			
			ImageIO.write(image, "jpg", file);			
			System.out.println("Wrote " + file.getAbsolutePath());			
		}
		catch (Exception e) {
			System.err.println("Caught error...");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Performs the data normalization. This algorithm is based
	 * on what I learned from 
	 * <a href="http://www.howcast.com/videos/359111-How-to-Normalize-Data">this</a> 
	 * video
	 */
	private void normalize() {
		for(int i = 0; i < bytes.length; i++) {
			int x = data[i];
			int v = (int) Math.floor(x * (255.0 / 4096.0));
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
