package main.LSB;

import java.util.ArrayList;
import java.util.List;

import main.JPEG.DCTMatrix;
import main.Util.DCTStream;

public class DCTJsteg implements DCTMatrixLSB {

	@Override
	public List<List<DCTMatrix>> hideMessage(List<List<DCTMatrix>> stack, HiddenMessage message) {
		
		message = HiddenMessage.addLengthInformation(message);
		
		DCTStream dctstream = new DCTStream(stack);
		int position = 0;
		int max = dctstream.size;
		
		while(message.hasNext()) {
			int value = dctstream.getValueAt(position);
			while(value==0 || value==1 || position%64==0/*skip DC components*/) {
				position++;
				value = dctstream.getValueAt(position);
			}
			
			int bit = message.getNext();
			if(bit==0 && Math.abs(value%2)==1) {
				value--;
			}else if(bit==1 && Math.abs(value%2)==0) {
				value++;
			}
			
			dctstream.setValueAt(position, value);
			position++;
		}
		
		return dctstream.data;
	}
	
	

	@Override
	public HiddenMessage recoverMessage(List<List<DCTMatrix>> stack) {
		
		DCTStream stream = new DCTStream(stack);
		int headerIndex = 0;
		byte[] header = {0,0,0,0,-1};
		int messageLength = 0;
		int bitsRead = 0;
		int actual = 0;		
		int position = 0;
		
		
		while(headerIndex<5) {
			int value = stream.getValueAt(position);
			while(value==0 || value==1 || position%64==0) {
				position++;
				value = stream.getValueAt(position);
			}
			
			actual*=(byte)2;
			actual+=(byte)Math.abs(value%2);
			bitsRead++;
			
			if(bitsRead==8) {
				header[headerIndex] = (byte)actual;
				headerIndex++;
				actual = 0;
				bitsRead = 0;
			}
			
			position++;
		}
		
		if(header[4]!=0) {
			System.err.println("Error in reconstruction of message");
		}
		messageLength = HiddenMessage.getLengthInfo(header); // header[0]*256*256*256+header[1]*256*256+header[2]*256+header[3];
		byte[] data = new byte[messageLength];
		int index = 0;
		
		while(index<messageLength) {
			int value = stream.getValueAt(position);
			while(value==0 || value==1 || position%64==0) {
				position++;
				value = stream.getValueAt(position);
			}
			
			actual*=(byte)2;
			actual+=(byte)Math.abs(value%2);
			bitsRead++;
			
			if(bitsRead==8) {
				data[index] = (byte)actual;
				index++;
				actual = 0;
				bitsRead = 0;
			}
			
			position++;
		}
		
		return new HiddenMessage(data);
	}

}
