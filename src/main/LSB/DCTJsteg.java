package main.LSB;

import java.util.ArrayList;
import java.util.List;

import main.JPEG.DCTMatrix;

public class DCTJsteg implements DCTMatrixLSB {

	@Override
	public List<List<DCTMatrix>> hideMessage(List<List<DCTMatrix>> stack, HiddenMessage message) {
		
		message = HiddenMessage.addEOMByte(message);
		
		int lineIndex = 0;
		int matrixIndex = 0;
		
		while(message.hasNext()) {
			
			if(lineIndex < stack.size()) {
				if(matrixIndex <stack.get(lineIndex).size()) {
					DCTMatrix matrix = stack.get(lineIndex).get(matrixIndex);
					
					int valueIndex = 1;
					while(message.hasNext() && valueIndex<64) {
						int value = matrix.getValue(valueIndex);
						if(value!=0 && value !=1) {
							int m_bit = message.getNext();
							if(value%2==0 && m_bit==1) {
								value++;
							}
							if(Math.abs(value%2)==1 && m_bit==0) {
								value--;
							}
						}
						matrix.setAC(valueIndex, value);
						valueIndex++;
					}
					matrixIndex++;
					
				}else {
					lineIndex++;
				}
			}else{
				System.out.println("no space!!");
				return stack;
			}
		}	
		
		return stack;
	}
	
	

	@Override
	public HiddenMessage recoverMessage(List<List<DCTMatrix>> stack) {
		//int bytesRead = 0;
		List<Byte> data = new ArrayList<Byte>(); //test implementation
		byte actuel = 0;
		byte readActuelBits = 0;
		
		int lineIndex = 0;
		int matrixIndex = 0;
		
		getIt: 
		for(List<DCTMatrix> lines : stack) {
			for(DCTMatrix matrix : lines) {
				for(int i=1;i<64;i++) {
					int value = matrix.getValue(i);
					if(value!=0 && value != 1) {
						readActuelBits++;
						actuel*=(byte)2;
						actuel+=(byte)Math.abs(value%2);						
						if(readActuelBits==8) {
							//data[bytesRead] = actuel;
							data.add(actuel);
							if(actuel == 0) {
								break getIt;
							}
							actuel = 0;
							//bytesRead++;
							readActuelBits =0;
						}
					}
				}
			}
		}
		
		System.out.println();
		System.out.println(data);
		System.out.println();
		data.remove(data.size()-1);
		Byte[] tmp = new Byte[data.size()];
		tmp = (Byte[]) data.toArray(tmp);
		byte[] dataArray = new byte[tmp.length];
		for(int i=0;i<tmp.length;i++) {
			dataArray[i] = tmp[i];
		}
		return new HiddenMessage(dataArray);		
	}

}
