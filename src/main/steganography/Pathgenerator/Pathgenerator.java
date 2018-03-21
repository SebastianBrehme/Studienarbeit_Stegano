package main.steganography.Pathgenerator;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import main.Util.DCTStream;

public class Pathgenerator {

	
	private DCTStream dctStream;
	private long seed;
	private Set<Integer> path;
	
	public Set<Integer> getPath() {
		return path;
	}

	public Pathgenerator(DCTStream dctstream, long seed){
		this.dctStream = dctstream;
		this.seed = seed;
		this.path = new LinkedHashSet<Integer>();
	}
	
	public Set<Integer> generatePath() {
		int numberOfDCTComponents = this.dctStream.size / 64;
		Random rn = new Random(this.seed);
		while (this.path.size() < numberOfDCTComponents) {
			Integer nextInt = rn.nextInt(numberOfDCTComponents);
			this.path.add(nextInt);
		}
		return this.path;
	}
	
	
}
