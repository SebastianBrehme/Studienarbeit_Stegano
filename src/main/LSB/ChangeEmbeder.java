package main.LSB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import main.JPEG.DCTMatrix;
import main.Util.DCTStream;
import main.Util.Pathgenerator;

public class ChangeEmbeder implements DCTMatrixLSB {

	Iterator<Integer> m_path;
	Set<Integer> usedPositions = null;
	
	@Override
	public List<List<DCTMatrix>> hideMessageWithKey(List<List<DCTMatrix>> stack, HiddenMessage message, String key) {
		usedPositions = new HashSet<Integer>();
		message = HiddenMessage.addLengthInformation(message);
		DCTStream stream = new DCTStream(stack);
		this.createPath(stream, key);
		
		while(message.hasNext()) {
			
			int position;
			try {
				position = this.nextBitLocation();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return null;
			}
			
			for(int offset = 1;offset<64 && message.hasNext();offset+=1) {
				int value = stream.getValueAt(position+offset);
				if(value==0 || value == 1) {
					continue;
				}
				int bit = message.getNext();
				this.usedPositions.add(position+offset);
				if(bit!=Math.abs(value%2)) {
					try {
						this.doChange(stream, position+offset, /*diff*/500, bit);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						return null;
					}
				}
			}
			
		}		
		return stack;
	}
	
	

	@Override
	public HiddenMessage recoverMessageWithKey(List<List<DCTMatrix>> stack, String key) {
		DCTStream stream = new DCTStream(stack);
		this.createPath(stream, key);
		
		int headerIndex = 0;
		byte[] header = {0,0,0,0,-1};
		int messageLength = 0;
		int bitsRead = 0;
		int actual = 0;		
		int position;
		try {
			position = this.nextBitLocation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new HiddenMessage();
		}
		
		int headerEndPos = 0;
		int headerEndBit = 0;
		
		List<Byte> tempList = new ArrayList<Byte>();
		
		while(headerIndex<5) {
			for(int offset = 1;offset<64;offset++) {
				int value = stream.getValueAt(position+offset);
				if(value==0 || value==1){//) || position+offset%64==0) {
					continue;
				}
				
				actual*=(byte)2;
				actual+=(byte)Math.abs(value%2);
				bitsRead++;
				
				if(bitsRead==8 && headerIndex<5) {
					header[headerIndex] = (byte)actual;
					headerIndex++;
					actual = 0;
					bitsRead = 0;
				}else if(bitsRead==8 && headerIndex>=5) {
					//System.out.println("adding to list: "+actual);
					tempList.add((byte)actual);
					actual = 0;
					bitsRead = 0;
					//break;
				}
				//if(headerIndex==5) {
				//	headerEndPos = position;
				//	headerEndBit = offset;
				//	break;
				//}
			}
			
			try {
				position = this.nextBitLocation();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return new HiddenMessage();
			}
		}
		
		if(header[4]!=0) {
			System.err.println("Error in reconstruction of message");
		}
		
		messageLength = HiddenMessage.getLengthInfo(header); // header[0]*256*256*256+header[1]*256*256+header[2]*256+header[3];
		byte[] data = new byte[messageLength];
		int index = 0;
		
		for(;index<tempList.size();index++) {
			data[index] = tempList.get(index);
		}
		
		while(index<messageLength) {
			for(int offset = 1;offset<64;offset++) {
				
				if(bitsRead==8) {
					data[index] = (byte)actual;
					index++;
					actual = 0;
					bitsRead = 0;
				}
				if(index==messageLength) {
					break;
				}
				
				int value = stream.getValueAt(position+offset);
				if(value==0 || value==1){//) || position+offset%64==0) {
					continue;
				}			
				
				actual*=(byte)2;
				actual+=(byte)Math.abs(value%2);
				bitsRead++;
				//if(headerIndex==5) {
				//	headerEndPos = position;
				//	headerEndBit = offset;
				//	break;
				//}
			}		
			try {
				position = this.nextBitLocation();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return new HiddenMessage();
			}
		}
		
		
		return new HiddenMessage(data);		
	}
	
	
	private void createPath(DCTStream stream, String key){
		Pathgenerator pg = new Pathgenerator(stream, key.hashCode());
		this.m_path = pg.generatePath().iterator();
	}
	
	private int nextBitLocation() throws Exception {
		if(m_path.hasNext()) {
			return m_path.next()*64;
		}
		System.err.println("ERROR no more path Locations");
		throw new Exception("ERROR no more path Locations");
	}

	private void doChange(DCTStream stream, int startPos, int difference, int bit) throws Exception {
		int startValue = stream.getValueAt(startPos);
		int position = startPos+1;
		int nextValue = stream.getValueAt(position);
		
		while(position % 64 ==0 || Math.abs(nextValue-startValue)>difference || Math.abs(nextValue % 2) != bit || nextValue==1 || nextValue==0 || this.usedPositions.contains(position)) {
			position++;
			position%=(stream.size);
			nextValue = stream.getValueAt(position);
			
			if(position == startPos) {
				//System.err.println("not possible!");
				throw new Exception("not possible Change value found");
			}
		}
		
		stream.setValueAt(position, startValue);
		stream.setValueAt(startPos, nextValue);
		
		//System.out.printf("Change %d with %d; Bit %d\n", startValue, nextValue, bit);
		//System.out.printf("Start: %d\tEnd: %d\tIn: %b\n", startPos, position, this.usedPositions.contains(position));
		
	}
}
