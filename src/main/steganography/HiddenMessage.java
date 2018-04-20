package main.steganography;

import java.util.Arrays;

import main.Util.BitConverter;

public class HiddenMessage {

	byte[] data;
	Type type;
	public byte[] filename ="<NOFILENAME>".getBytes();
	int byteIndex = 0;
	int bitIndex = 0;
	public final static int HEADERLENGTH = 6;
	
	public enum Type{
		TEXT,FILE, NOTDEFINED;
	}
	
	private static int factor = 128;

	public HiddenMessage() {
		data = new byte[0];
		type = Type.NOTDEFINED;
	}
	
	public byte[] getData() {
		return this.data;
	}
	
	public Type getType() {
		return this.type;
		
	}
	
	
	//public HiddenMessage(byte[] data) {
	//	this.data = data;	
	//}
	
	public HiddenMessage(byte[] data, Type type) {
		this.data = data;
		this.type = Type.TEXT;
	}
	
	public HiddenMessage(byte[] data, byte[] filename) {
		this.data = data;
		this.filename = filename;
		this.type = Type.NOTDEFINED;
		HiddenMessage fileHeader = new HiddenMessage(filename, Type.NOTDEFINED);
		this.addTwoHiddenMessages(fileHeader);
	}
	
	public HiddenMessage(byte[] data, byte[] filename, boolean create) {
		this.data = data;
		this.filename=filename;
		this.type = Type.TEXT;
	}
	
	public static HiddenMessage addHeaderInformation(HiddenMessage msg) {
		if(msg.type==Type.FILE) {
			return msg;
		}
		byte[] newData = new byte[msg.data.length+HEADERLENGTH];
		System.arraycopy(msg.data, 0, newData, HEADERLENGTH, msg.data.length);
		int length = msg.data.length;
		byte byte3 = (byte) (length%factor);
		byte byte2 = (byte)((length/factor)%factor);
		byte byte1 = (byte)(((length/factor)/factor)%factor);
		byte byte0 = (byte)((((length/factor)/factor)/factor)%factor);
		
		newData[0] = byte0;
		newData[1] = byte1;
		newData[2] = byte2;
		newData[3] = byte3;
		newData[4] = (byte) msg.getType().ordinal();
		newData[5] = 0;
		return new HiddenMessage(newData, msg.getType());
	}
	
	private void addTwoHiddenMessages(HiddenMessage summand) {
		summand = addHeaderInformation(summand);	
		summand.data[4] = (byte) Type.FILE.ordinal();
		HiddenMessage withHeader = addHeaderInformation(this);
		withHeader.data[4] = (byte) Type.FILE.ordinal();
		
		int length = summand.data.length+withHeader.data.length;
		byte[] new_data = new byte[length];
		System.arraycopy(summand.data,0,new_data, 0, summand.data.length);
		System.arraycopy(withHeader.data, 0, new_data, summand.data.length, withHeader.data.length);
		
		this.data = new_data;
		this.type = Type.FILE;
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
		String bits = BitConverter.convertSignedToBitString(actuel); //the bytes of a string are signed: values from -128 to 127; must be taken into account when calculated
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
		//return "HiddenMessage [String=("+new String(data)+")] [data=" + Arrays.toString(data) + "]";
		return "HiddenMessage [data="+ Arrays.toString(data)+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HiddenMessage other = (HiddenMessage) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}
	
	
	

}
