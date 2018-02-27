package main.Util;

import java.util.List;

import main.JPEG.DCTMatrix;

public class DCTStream {

	public List<List<DCTMatrix>> data;
	private int firstIndex = 0;
	private int secondIndex = 0;
	private int dctIndex = 0;
	
	public int size = 0;
	
	public DCTStream(List<List<DCTMatrix>> data) {
		this.data = data;
		
		for(List<DCTMatrix> tmp : this.data) {
			size+= tmp.size()*64;
		}
	}
	
	public int getValueAt(int position) {
		Position pos = new Position(position);
		return this.data.get(pos.fIndex).get(pos.sIndex).getValue(pos.dctIndex);
	}
	
	public void setValueAt(int position, int value) {
		Position pos = new Position(position);
		this.data.get(pos.fIndex).get(pos.sIndex).setValue(pos.dctIndex,value);
	}
	
	
	private class Position{
		private int fIndex = 0;
		private int sIndex = 0;
		private int dctIndex = 0;
		
		public Position(int position) {
			dctIndex = position%64;
			sIndex = position/64;
			while(DCTStream.this.data.get(fIndex).size()<=sIndex) {
				sIndex -= DCTStream.this.data.get(fIndex).size();
				fIndex++;
			}
		}
	}
}
