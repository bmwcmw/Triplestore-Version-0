package dataCompressor;

import java.util.HashMap;
import java.util.HashSet;

/**
 * NOT USED
 * S-O or O-S hashing structure using converted index, not litteral data
 * @author CMWT420
 */
public class UnusedSOOSStructure {
	private HashMap<Integer, HashSet<Integer>> _entries;
	
	public UnusedSOOSStructure(){
		this._entries = new HashMap<Integer, HashSet<Integer>>();
	}
	
	public void insert(Integer key, Integer value){
		if(this.getStructure().containsKey(key)){
			this._entries.get(key).add(value);
		} else {
			HashSet<Integer> valset = new HashSet<Integer>();
			valset.add(value);
			this._entries.put(key, valset);
		}
	}
	
	public HashSet<Integer> getLine(Integer lineNb){
		return this._entries.get(lineNb);
	}
	
	public HashMap<Integer, HashSet<Integer>> getStructure(){
		return this._entries;
	}
}