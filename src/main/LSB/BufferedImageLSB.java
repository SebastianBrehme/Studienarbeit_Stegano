package main.LSB;

import java.awt.Color;
import java.awt.image.BufferedImage;
import main.Util.BitConverter;

public class BufferedImageLSB {

	private BufferedImage m_image;
	private String hiddenData;

	public BufferedImageLSB(BufferedImage m_image, String hiddenData) {
		super();
		this.m_image = m_image;
		this.hiddenData = hiddenData;
	}

	public int maximumBits() {
		return this.m_image.getHeight() * this.m_image.getWidth() * 3 / 8;
	}

	public BufferedImage encode() {
		hiddenData = String.valueOf(hiddenData.length()) + ";" + hiddenData;

		int bitDataLenght = hiddenData.length() * 8;
		if (bitDataLenght > this.maximumBits()) {
			System.out.println("Hidden data to long!!");
			return null;
		}

		byte hiddenByteData[] = hiddenData.getBytes();
		String bitString = "";
		for (byte b : hiddenByteData) {
			bitString += BitConverter.convertToBitString(b);
		}

		int position = 0;
		
		for(int w =0; w<m_image.getWidth();w++) {
			for(int h = 0;h<m_image.getHeight();h++) {
				Color color = new Color(m_image.getRGB(w, h));
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				
				if(position<bitString.length()) {
					if(red%2 == 0) {
						if(bitString.charAt(position) == '1') {
							red++;
						}
					}else if(red%2 ==1) {
						if(bitString.charAt(position)=='0') {
							red--;
						}
					}
					position++;
				}
				
				if(position<bitString.length()) {
					if(green%2 == 0) {
						if(bitString.charAt(position) == '1') {
							green++;
						}
					}else if(green%2 ==1) {
						if(bitString.charAt(position)=='0') {
							green--;
						}
					}
					position++;
				}
				
				if(position<bitString.length()) {
					if(blue%2 == 0) {
						if(bitString.charAt(position) == '1') {
							blue++;
						}
					}else if(blue%2 ==1) {
						if(bitString.charAt(position)=='0') {
							blue--;
						}
					}
					position++;
				}
				
				m_image.setRGB(w, h, new Color(red,green,blue).getRGB());				
			}
		}
		
		return m_image;
	}

	public String decode() {
		
		String bitString = "";
		
		for(int w=0;w<m_image.getWidth();w++) {
			for(int h=0;h<m_image.getHeight();h++) {
				Color color = new Color(m_image.getRGB(w, h));
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				
				if(red%2==0) {
					bitString+="0";
				}else {
					bitString += "1";
				}
				
				if(green%2==0) {
					bitString+="0";
				}else {
					bitString += "1";
				}
				
				if(blue%2==0) {
					bitString+="0";
				}else {
					bitString += "1";
				}
			}
		}
		
		String sep = ";";
		int position = 0;
		String length = "";
		String last = "";
		while(!last.equals(sep)) {
			String temp = bitString.substring(position, position+8);
			char zeichen = (char) this.convertBits(temp);
			last = String.valueOf(zeichen);
			if(!last.equals(sep)) {
				length +=last;
			}
			position+=8;
		}
		
		int bitcount = 8*Integer.parseInt(length)+position; //bits
		String result = "";
		
		while(position < bitcount) {
			String temp = bitString.substring(position, position+8);
			char zeichen = (char) this.convertBits(temp);
			result += String.valueOf(zeichen);
			position+=8;
		}
		
		return result;		
	}

	private int convertBits(String bitString) {
		int result = 0;
		for (int i = 0; i < bitString.length(); i++) {
			int v = (int) Math.pow(2, i);
			if (bitString.charAt(bitString.length() - 1 - i) == '1') {
				result += v;
			}
		}
		return result;
	}
}
