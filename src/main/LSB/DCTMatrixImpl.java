package main.LSB;

import java.util.List;

import main.JPEG.DCTMatrix;

public class DCTMatrixImpl implements DCTMatrixLSB {

	@Override
	public List<List<DCTMatrix>> hideMessage(List<List<DCTMatrix>> stack, HiddenMessage message) {
		// TODO Auto-generated method stub
		System.out.println(stack.size());
		int sum = 0;
		for(List<DCTMatrix> temp : stack) {
			sum+=temp.size();
		}
		System.out.println(sum);
		return stack;
	}

	@Override
	public HiddenMessage recoverMessage(List<List<DCTMatrix>> stack) {
		// TODO Auto-generated method stub
		return null;
	}

}
