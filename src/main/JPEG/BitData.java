package main.JPEG;

import java.util.ArrayList;
import java.util.List;

import main.Util.BitConverter;

public class BitData {
	private List<List<Integer>> byteData;
	private byte[][] bitData;
	
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
		return actIndex+8>=bitData[actList].length;
	}
	
	public String cutPartAsString(int start, int end) {
		start+=actIndex;
		end+=actIndex;
		String result = "";
		for(int i = start;i<end;i++) {
			if(bitData[actList].length>i) {
				if(bitData[actList][i] == 0) {
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
		return actList+1<this.bitData.length;
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
		bitData = new byte[byteData.size()][];//ArrayList<List<Byte>>();
		int n = 0;
		for(List<Integer> tmp: byteData) {
			bitData[n] = integerDataToBits(tmp);
			n++;
		}
	}

	private byte[] integerDataToBits(List<Integer> tmp) {
		byte[] result = new byte[tmp.size()*8];// ArrayList<Byte>(tmp.size());
		int n =0;
		for(Integer i:tmp) {
			String t = BitConverter.convertToBitString(i);
			for(int j=0;j<t.length();j++) {
				if(t.charAt(j)=='0') {
					result[n] = ((byte)0);
				}
				else {
					result[n] = ((byte) 1);
				}
				n++;
			}
		}
		return result;
	}
}
