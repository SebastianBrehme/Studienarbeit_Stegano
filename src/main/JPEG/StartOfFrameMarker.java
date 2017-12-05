package main.JPEG;

import java.util.List;

import main.Util.BitConverter;

public class StartOfFrameMarker {
	
	private List<Integer> content;
	
	private int precision;
	private int lines;
	private int samplesPerLine;
	private int numberOfComponents;
	
	private int[] HorizontalFactors;
	private int[] VerticalFactors;
	
	private int YHorizontalFactor;
	private int YVerticalFactor;
	
	private int CbHorizontalFactor;
	private int CbVerticalFactor;
	
	private int CrHorizontalFactor;
	private int CrVerticalFactor;

	public StartOfFrameMarker(List<Integer> markerContent)
	{
		this.content = markerContent;
		this.extractInformation();
	}
	
	private void extractInformation()
	{
		int index = 0;
		int length0 = this.content.get(index++);
		int length1 = this.content.get(index++);
		int length = 16 * length0 + length1;
		this.precision = this.content.get(index++);
		int lines0 = this.content.get(index++);
		int lines1 = this.content.get(index++);
		this.lines = 16 * lines0 + lines1;
		int samplesPerLine0 = this.content.get(index++);
		int samplesPerLine1 = this.content.get(index++);
		this.samplesPerLine = 16 * samplesPerLine0 + samplesPerLine1;
		this.numberOfComponents = this.content.get(index++);
		this.HorizontalFactors = new int[this.numberOfComponents];
		this.VerticalFactors = new int[this.numberOfComponents];
		for (int i = 0; i < this.numberOfComponents; i++)
		{
			int componentIdentifier = this.content.get(index++);
			int factors = this.content.get(index++);
			if (componentIdentifier == 1)
			{
				this.HorizontalFactors[0] = BitConverter.getHigherBits(factors);
				this.VerticalFactors[0] = BitConverter.getLowerBits(factors);
				this.YHorizontalFactor = BitConverter.getHigherBits(factors);
				this.YVerticalFactor = BitConverter.getLowerBits(factors);
			}
			else if (componentIdentifier == 2)
			{
				this.HorizontalFactors[1] = BitConverter.getHigherBits(factors);
				this.VerticalFactors[1] = BitConverter.getLowerBits(factors);
				this.CbHorizontalFactor = BitConverter.getHigherBits(factors);
				this.CbVerticalFactor = BitConverter.getLowerBits(factors);
			}
			else if (componentIdentifier == 3)
			{
				this.HorizontalFactors[2] = BitConverter.getHigherBits(factors);
				this.VerticalFactors[2] = BitConverter.getLowerBits(factors);
				this.CrHorizontalFactor = BitConverter.getHigherBits(factors);
				this.CrVerticalFactor = BitConverter.getLowerBits(factors);
			}
		}
	}

	
	//Getter 
	public int[] getHorizontalFactors()
	{
		return this.HorizontalFactors;
	}
	
	public int[] getVerticalFactors()
	{
		return this.VerticalFactors;
	}
	public int getCrVerticalFactor() {
		return CrVerticalFactor;
	}

	public int getCrHorizontalFactor() {
		return CrHorizontalFactor;
	}

	public int getCbVerticalFactor() {
		return CbVerticalFactor;
	}

	public int getCbHorizontalFactor() {
		return CbHorizontalFactor;
	}

	public int getYVerticalFactor() {
		return YVerticalFactor;
	}

	public int getYHorizontalFactor() {
		return YHorizontalFactor;
	}

	public int getPrecision() {
		return precision;
	}

	public int getLines() {
		return lines;
	}

	public int getSamplesPerLine() {
		return samplesPerLine;
	}

	public int getNumberOfComponents() {
		return numberOfComponents;
	}


}
