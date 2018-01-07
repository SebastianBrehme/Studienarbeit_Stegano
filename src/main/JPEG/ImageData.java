package main.JPEG;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import main.Util.BitConverter;

public class ImageData {
	List<Integer> rawData;
	List<String> imageDataStrings;
	int startSpectralSelection;
	int endSpectralSelection;
	int successivApproximation;
	int firstLength;
	int secoundLength;
	Component[] components;
	
	public ImageData(List<Integer> rawData) {
		this.rawData = rawData;
		this.imageDataStrings = new ArrayList<>();
	}
	
	public void sortRawData() {
		Iterator<Integer> rawIt = rawData.iterator();
		this.firstLength = rawIt.next();
		this.secoundLength = rawIt.next();
		
		this.components = new Component[rawIt.next()];
		for(int i=0;i<components.length;i++) {
			components[i] = new Component(rawIt.next(), rawIt.next());
		}
		
		this.startSpectralSelection = rawIt.next();
		this.endSpectralSelection = rawIt.next();
		this.successivApproximation = rawIt.next();
		
		List<Integer> rawInteger = new ArrayList<Integer>(rawData.size());
		while(rawIt.hasNext()) {
			rawInteger.add(rawIt.next());
		}
		List<List<Integer>> temp = this.splitRestartMarker(rawInteger);
		temp = this.substitudeFF00(temp);
		this.imageDataStrings = this.convertIntegerDataToBits(temp);		
	}
	
	public List<List<Integer>> substitudeFF00(List<List<Integer>> rawInteger){
		List<List<Integer>> processedInteger = new ArrayList<List<Integer>>();
		for(List<Integer> temp : rawInteger) {
			processedInteger.add(this.substitudeFF00Helper(temp));
		}
		return processedInteger;
	}
	
	private List<Integer> substitudeFF00Helper(List<Integer> rawInteger) {
		List<Integer> processedInteger = new ArrayList<Integer>(rawInteger.size());
		processedInteger.add(rawInteger.get(0));
		for(int i=1;i<rawInteger.size();i++) {
			if(rawInteger.get(i-1)==0xFF && rawInteger.get(i)==0x00) {
				//processedInteger.add(0xFF);
				//i++;
			}else {
				processedInteger.add(rawInteger.get(i));
			}
		}
		return processedInteger;
	}
	
	public List<List<Integer>> splitRestartMarker(List<Integer> rawInteger){
		List<List<Integer>> processedInteger = new ArrayList<List<Integer>>();
		List<Integer> temp = new ArrayList<>();
		
		for(int i=0;i<rawInteger.size()-1;i++) {
			if(rawInteger.get(i)==0xFF && rawInteger.get(i+1)>=0xD0 && rawInteger.get(i+1) <= 0xD7) {
				processedInteger.add(temp);
				temp = new ArrayList<>();
				i++;
			}else {
				temp.add(rawInteger.get(i));
			}
		}
		temp.add(rawInteger.get(rawInteger.size()-1));
		processedInteger.add(temp);
		return processedInteger;
	}
	
	
	public List<String> convertIntegerDataToBits(List<List<Integer>> rawInteger) {
		List<String> result = new ArrayList<>();
		for(List<Integer> list:rawInteger) {
			result.add(integerToBit(list));
		}
		return result;
	}
	
	private String integerToBit(List<Integer> rawInteger) {
		StringBuilder result = new StringBuilder();
		for(int i : rawInteger) {
			result.append(BitConverter.convertToBitString(i));
		}
		return result.toString();
	}
	
	
	class Component{
		int ID;
		int DC;
		int AC;
		
		public Component(int id, int dcac) {
			this.ID = id;
			this.DC = BitConverter.getHigherBits(dcac);
			this.AC = BitConverter.getLowerBits(dcac);
		}
	}
}
