package main.LSB;

import java.util.List;

import main.JPEG.DCTMatrix;

public interface DCTMatrixLSB {
	public default List<List<DCTMatrix>> hideMessage(List<List<DCTMatrix>> stack, HiddenMessage message){
		System.out.println("NO CHANGES APPLY");
		return stack;
	}
	public default HiddenMessage recoverMessage(List<List<DCTMatrix>> stack) {
		return new HiddenMessage();
	}
	
	public default List<List<DCTMatrix>> hideMessageWithKey(List<List<DCTMatrix>> stack, HiddenMessage message, String key){
		System.out.println("NO CHANGES APPLY");
		return stack;
	}
	
	public default HiddenMessage recoverMessageWithKey(List<List<DCTMatrix>> stack, String key) {
		return new HiddenMessage();
	}
}
