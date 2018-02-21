package main.LSB;

import java.util.Arrays;

import main.Util.BitConverter;

public class HiddenMessage {

	byte[] data;
	int byteIndex = 0;
	int bitIndex = 0;

	public HiddenMessage() {
		data = new byte[0];
	}
	
	public HiddenMessage(String msg) {
		this.data = msg.getBytes();
	}
	
	public HiddenMessage(byte[] data) {
		this.data = data;
		
	}
	
	public static HiddenMessage addEOMByte(HiddenMessage msg) {
		byte[] newData = Arrays.copyOf(msg.data, msg.data.length+1);
		newData[newData.length-1] = (byte) 0;
		HiddenMessage result =  new HiddenMessage(newData);
		return result;
	}
	
	public static HiddenMessage addLengthInfor(HiddenMessage msg) {
		byte[] newData = new byte[msg.data.length+5];
		System.arraycopy(msg.data, 0, newData, 5, msg.data.length);
		int length = msg.data.length;
		byte byte3 = (byte) (length%256);
		byte byte2 = (byte)((length/256)%256);
		byte byte1 = (byte)(((length/256)/256)%256);
		byte byte0 = (byte)((((length/256)/256)/256)%256);
		
		newData[0] = byte0;
		newData[1] = byte1;
		newData[2] = byte2;
		newData[3] = byte3;
		newData[4] = 0;
		return new HiddenMessage(newData);
	}
	
	public boolean hasNext() {
		return byteIndex < data.length;
	}

	public int bitLength() {
		return data.length*8;
	}
	
	public int getNext() {
		byte actuel = data[byteIndex];
		String bits = BitConverter.convertToBitString(actuel);
		int result =  Integer.parseInt(""+bits.charAt(bitIndex));
		
		bitIndex++;
		if(bitIndex==8) {
			bitIndex =0;
			byteIndex++;
		}
		
		return result;		
	}

	@Override
	public String toString() {
		return "HiddenMessage [String=("+new String(data)+")] [data=" + Arrays.toString(data) + "]";
	}
	
	

}
