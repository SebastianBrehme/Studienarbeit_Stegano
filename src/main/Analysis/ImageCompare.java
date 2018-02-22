package main.Analysis;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageCompare {
	
	public enum DiffImageMode{
		DiffImage,
		AbsolutDiffImage,
		NoDiffImage;
	}
	
	public enum Calculate{
		Linear,
		Quadratic;
	}
	
	
	private static BufferedImage loadImage(File file) throws IOException {
		BufferedImage input = ImageIO.read(file);
		return input;
	}
	
	public static long compareImages(String pathToOriginal, String pathToChanged, DiffImageMode mode, int exponent) throws IOException {
		return ImageCompare.compareImages(new File(pathToOriginal), new File(pathToChanged), mode, exponent);
	}
	
	/**
	 * 	
	 * @param originalFile
	 * @param changedFile
	 * @param mode - should there be a difference Image and if should it be the real difference values or only 0 and 255
	 * @param exponent - the Power of the differences which get summed up 
	 * @return long - how equal are the Images on scale from 0 to 1
	 * @throws IOException
	 */
	public static long compareImages(File originalFile, File changedFile, DiffImageMode mode, int exponent) throws IOException {
		BufferedImage original = ImageCompare.loadImage(originalFile);
		BufferedImage changed = ImageCompare.loadImage(changedFile);
		
		long difference = 0;
		BufferedImage image = new BufferedImage(original.getWidth(),original.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for(int i=0;i<original.getWidth();i++) {
			for(int j=0;j<original.getHeight();j++) {
				Color c_org = new Color(original.getRGB(i, j));
				Color c_ch = new Color(changed.getRGB(i, j));
				int reddiff = (int)Math.abs(c_org.getRed()-c_ch.getRed());
				int greendiff = (int)Math.abs(c_org.getGreen()-c_ch.getGreen());
				int bluediff = (int)Math.abs(c_org.getBlue()-c_ch.getBlue());				
				
				image.setRGB(i, j, calculateColor(reddiff, greendiff, bluediff, mode).getRGB());				
				
				difference += Math.abs((int)Math.pow(reddiff,exponent));
				difference += Math.abs((int)Math.pow(greendiff,exponent));
				difference += Math.abs((int)Math.pow(bluediff,exponent));
			}
		}
		
		if(mode != DiffImageMode.NoDiffImage) {
			ImageIO.write(image, "png", new File("TestImage"+File.separator+"difference.png"));
		}
		
		long maximalValue = original.getWidth()*original.getHeight()*(3*255);
		System.out.printf("Zu %f %s gleich", (1.0-(double)difference/(double)maximalValue)*100,"%");
		return difference;
	}
	
	private static Color calculateColor(int reddiff, int greendiff, int bluediff, DiffImageMode mode) {
		if(mode == DiffImageMode.AbsolutDiffImage) {
			if(reddiff+greendiff+bluediff == 0) {
				return new Color(0,0,0);
			}
			return new Color(255,255,255);
		}
		return new Color(reddiff,greendiff,bluediff);
	}
}
