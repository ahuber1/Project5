package hubble;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import util.Sorter;

/**
 * Processes the data that a {@link Reciever} received
 * @author Andrew Huber
 */
public class Processor {

	/** The second buffer, B2, as specified in the project document */
	private IntegerBuffer b2;
	
	/** The value T, as specified in the project document */
	private int t;
	
	/** The value N, as specified in the project document */
	private int n;
	
	/** The data in B2 as an array */
	private int[] data;
	
	/** Will contain the normalized data */
	private int[] normalized;
	
	/** Will contain the file path of where the image is stored */
	private String path;
	
	/** Keeps track of how long it took to perform merge sort on the data in milliseconds */
	private long mergesortTime;
	
	/**
	 * Creates an object that will process the data that a {@link Reciever} recieved 
	 * @param b2 the second buffer, B2, as specified in the project document
	 * @param t the value T as specified in the project document
	 * @param n the value N as specified in the project document
	 */
	public Processor(IntegerBuffer b2, int t, int n) {
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
			long startTime = System.currentTimeMillis();
			data = Sorter.mergesort(b2.toArray(), t);
			mergesortTime = System.currentTimeMillis() - startTime;			
						
			normalized = new int[data.length];
			
			normalize();
			
			File file = new File("images");
			
			if(file.exists() == false)
				file.mkdir();
			
			path = String.format("images/output_N%d_T%d.jpg", n, t);
			file = new File(path);
			
			if(file.exists())
				file.delete();
			
			BufferedImage image = new BufferedImage((int) Math.sqrt(normalized.length),
					(int) Math.sqrt(normalized.length), BufferedImage.TYPE_BYTE_GRAY);
			
			WritableRaster raster = image.getRaster();
					
			for(int i = 0, iter = 0; i < image.getHeight(); i++) {
				for(int j = 0; j < image.getHeight(); j++, iter++) {
					raster.setPixel(i, j, new int[] {normalized[iter]});
				}
			}
			
			ImageIO.write(image, "jpg", file);			
		}
		catch (Exception e) {
			System.err.println("Caught error...");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Normalizes the data stored in {@link Processor#data data} between 0 and 255.
	 * <i>Because the {@link BufferedImage} is generated differently and requires values between
	 * 0 and 255 to represent pixel colors, this normalization methods differs from the one
	 * outlined in the project description.</i> 
	 */
	private void normalize() {
		for(int i = 0; i < normalized.length; i++) {
			int x = data[i];
			int v = (int) Math.floor(x * (255.0 / 4096.0));
			normalized[i] = v;
		}
	}
	
	/**
	 * Returns the relative path of where the saved image is stored
	 * @return the relative path of where the saved image is stored
	 */
	public String getRelativeFilePath() {
		return path;
	}
	
	/** 
	 * Returns the amount of time it took to sort the data in milliseconds
	 * @return the amount of time it took to sort the data in milliseconds 
	 */
	public long getMergesortTime() {
		return mergesortTime;
	}
}
