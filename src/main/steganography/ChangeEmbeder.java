package main.steganography;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import main.JPEG.DCTMatrix;
import main.Util.DCTStream;
import main.steganography.HiddenMessage.Type;
import main.steganography.Pathgenerator.Pathgenerator;

public class ChangeEmbeder implements DCTMatrixLSB {

	Iterator<Integer> m_path;
	Set<Integer> usedPositions = null;

	@Override
	public List<List<DCTMatrix>> hideMessageWithKey(List<List<DCTMatrix>> stack, HiddenMessage message, String key) {
		usedPositions = new HashSet<Integer>();
		message = HiddenMessage.addHeaderInformation(message);
		DCTStream stream = new DCTStream(stack);
		this.createPath(stream, key);
		try {
			while (message.hasNext()) {

				int position = this.nextBitLocation();

				for (int offset = 1; offset < 64 && message.hasNext(); offset++) {
					int value = stream.getValueAt(position + offset);
					if (value == 0 || value == 1) {
						continue;
					}
					int bit = message.getNext();
					this.usedPositions.add(position + offset);
					if (bit != Math.abs(value % 2)) {
						this.doChange(stream, position + offset, /* diff */500, bit);
					}
				}

			}
		} catch (NoPathException | NoChangePossibleException e) {
			e.printStackTrace();
			return null;
		}
		return stack;
	}

	@Override
	public HiddenMessage recoverMessageWithKey(List<List<DCTMatrix>> stack, String key) {
		DCTStream stream = new DCTStream(stack);
		this.createPath(stream, key);

		byte[] header = { 0, 0, 0, 0, 0, -1 };
		int messageLength = 0;
		int bitsRead = 0;
		int actual = 0;
		try {
		} catch (Exception e) {
			e.printStackTrace();
			return new HiddenMessage();
		}

		List<Byte> tempList = new ArrayList<Byte>();


		int headerIndex = 0;
		while (headerIndex < HiddenMessage.HEADERLENGTH) {
			int position = 0;
			try {
				position = this.nextBitLocation();
			} catch (NoPathException e1) {
				e1.printStackTrace();
			}
			for (int offset = 1; offset < 64; offset++) {
				int value = stream.getValueAt(position + offset);
				if (value == 0 || value == 1) {
					continue;
				}

				actual *= (byte) 2;
				actual += (byte) Math.abs(value % 2);
				bitsRead++;

				if (bitsRead == 8 && headerIndex < HiddenMessage.HEADERLENGTH) {
					header[headerIndex] = (byte) actual;
					headerIndex++;
					actual = 0;
					bitsRead = 0;
				} else if (bitsRead == 8 && headerIndex >= 5) {
					tempList.add((byte) actual);
					actual = 0;
					bitsRead = 0;
				}
			}
		}

		if (header[5] != 0) {
			System.err.println("Error in reconstruction of message");
		}

		messageLength = HiddenMessage.getLengthInfo(header);
		byte[] data = new byte[messageLength];
		int index = 0;

		for (; index < tempList.size() && messageLength!=0; index++) {
			data[index] = tempList.get(index);
		}
		
		while (index < messageLength) {
			int position =0;
			try {
				position = this.nextBitLocation();
			} catch (NoPathException e1) {
				e1.printStackTrace();
			}
			for (int offset = 1; offset < 64; offset++) {

				if (bitsRead == 8) {
					data[index] = (byte) actual;
					index++;
					actual = 0;
					bitsRead = 0;
				}

				if (index == messageLength) {
					break;
				}

				int value = stream.getValueAt(position + offset);
				if (value == 0 || value == 1) {
					continue;
				}

				actual *= (byte) 2;
				actual += (byte) Math.abs(value % 2);
				bitsRead++;
			}
		}

		Type type = Type.TEXT.ordinal() == header[4] ? Type.TEXT : Type.FILE;
		return new HiddenMessage(data, type);
	}

	private void createPath(DCTStream stream, String key) {
		Pathgenerator pg = new Pathgenerator(stream, key.hashCode());
		this.m_path = pg.generatePath().iterator();
	}

	private int nextBitLocation() throws NoPathException {
		if (m_path.hasNext()) {
			return m_path.next() * 64;
		}
		throw new NoPathException("ERROR no more path Locations");
	}

	private void doChange(DCTStream stream, int startPos, int difference, int bit) throws NoChangePossibleException {
		int startValue = stream.getValueAt(startPos);
		int position = startPos + 1;
		position %= (stream.size);
		int nextValue = stream.getValueAt(position);

		while (position % 64 == 0 || Math.abs(nextValue - startValue) > difference || Math.abs(nextValue % 2) != bit
				|| nextValue == 1 || nextValue == 0 || this.usedPositions.contains(position)) {
			position++;
			position %= (stream.size);
			nextValue = stream.getValueAt(position);

			if (position == startPos) {
				throw new NoChangePossibleException("not possible Change value found");
			}
		}

		stream.setValueAt(position, startValue);
		stream.setValueAt(startPos, nextValue);
	}

	private class NoChangePossibleException extends Exception {
		public NoChangePossibleException(String exceptionMessage) {
			super(exceptionMessage);
		}
	}

	private class NoPathException extends Exception {
		public NoPathException(String exceptionMessage) {
			super(exceptionMessage);
		}
	}
}
