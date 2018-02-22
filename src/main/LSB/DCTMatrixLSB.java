package main.LSB;

import java.util.List;

import main.JPEG.DCTMatrix;

public interface DCTMatrixLSB {
	public List<List<DCTMatrix>> hideMessage(List<List<DCTMatrix>> stack, HiddenMessage message);
	
	public HiddenMessage recoverMessage(List<List<DCTMatrix>> stack);
}
