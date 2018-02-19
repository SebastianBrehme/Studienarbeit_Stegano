package main.JPEG;

import java.util.ArrayList;
import java.util.List;

import main.Util.BitConverter;

public class BitData {
	private List<List<Integer>> byteData;
	private List<List<Byte>> bitData;
	
	private int actList = 0;
	private int actIndex = 0;
	
	public BitData(List<List<Integer>> byteData) {
		this.byteData = byteData;
		createBitData();
		
	}
	
	public void reduce(int start, int end) {
		actIndex+=start;
	}
	
	public boolean lessThan8() {
		return actIndex+8>bitData.get(actList).size();
	}
	
	public String cutPartAsString(int start, int end) {
		start+=actIndex;
		end+=actIndex;
		String result = "";
		for(int i = start;i<end;i++) {
			if(bitData.get(actList).size()>i) {
				if(bitData.get(actList).get(i) == 0) {
					result+="0";
				}else {
					result+="1";
				}
			}else {
				//return result;
			}
		}
		return result;
	}
	
	public void nextList() {
		actList++;
		actIndex = 0;
	}
	
	public boolean hasNextList() {
		return actList+1<this.bitData.size();
	}
	
	
	public boolean startsWithString(String compare) {
		try{
			String start = cutPartAsString(0, compare.length());
			return start.equals(compare);
		}catch(IndexOutOfBoundsException e) {
			return false;
		}
		
	}
	
	private void createBitData() {
		bitData = new ArrayList<List<Byte>>();
		for(List<Integer> tmp: byteData) {
			bitData.add(integerDataToBits(tmp));
		}
	}

	private List<Byte> integerDataToBits(List<Integer> tmp) {
		List<Byte> result = new ArrayList<Byte>(tmp.size()*8);
		for(Integer i:tmp) {
			String t = BitConverter.convertToBitString(i);
			for(int j=0;j<t.length();j++) {
				if(t.charAt(j)=='0') {
					result.add((byte)0);
				}
				else {
					result.add((byte) 1);
				}
			}
		}
		return result;
	}
}
