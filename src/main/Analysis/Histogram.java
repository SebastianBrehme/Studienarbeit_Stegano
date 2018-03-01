package main.Analysis;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import main.JPEG.DCTMatrix;
import main.Util.DCTStream;

public class Histogram {

	public static int[] createHistogram(List<List<DCTMatrix>> data) {
		DCTStream stream = new DCTStream(data);
		int offset = 5;
		int[] histogram = new int[11];
		
		for(int i=0;i<stream.size;i++) {
			try {
				int value = stream.getValueAt(i);
				histogram[value+offset]++;
			}catch(Exception e) {}
		}
		System.out.println(Arrays.toString(histogram));
		return histogram;
	}
}
