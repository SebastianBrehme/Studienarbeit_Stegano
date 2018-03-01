package main.LSB;

import java.util.Arrays;

import main.Util.BitConverter;

public class HiddenMessage {

	byte[] data;
	int byteIndex = 0;
	int bitIndex = 0;
	
	private static int factor = 128;

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
	
	public static HiddenMessage addLengthInformation(HiddenMessage msg) {
		byte[] newData = new byte[msg.data.length+5];
		System.arraycopy(msg.data, 0, newData, 5, msg.data.length);
		int length = msg.data.length;
		byte byte3 = (byte) (length%factor);
		byte byte2 = (byte)((length/factor)%factor);
		byte byte1 = (byte)(((length/factor)/factor)%factor);
		byte byte0 = (byte)((((length/factor)/factor)/factor)%factor);
		
		newData[0] = byte0;
		newData[1] = byte1;
		newData[2] = byte2;
		newData[3] = byte3;
		newData[4] = 0;
		return new HiddenMessage(newData);
	}
	
	public static int getLengthInfo(byte header[]) {
		for(int i=0;i<5;i++) {
			if(header[i]<0) {
				header[i] = (byte) (255-header[i]);
			}
		}
		
		return header[0]*factor*factor*factor+header[1]*factor*factor+header[2]*factor+header[3];
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
