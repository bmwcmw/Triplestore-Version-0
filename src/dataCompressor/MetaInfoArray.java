package dataCompressor;

import java.util.ArrayList;

/**
 * Data structure for a list of Meta Info triple
 * @author Cedar
 *
 */
public class MetaInfoArray {

	private ArrayList<MetaInfoTriple> list;
	
	public MetaInfoArray(){
		list = new ArrayList<MetaInfoTriple>();
	}
	
	public ArrayList<MetaInfoTriple> getList(){
		return list;
	}
	
	public void add(MetaInfoTriple m){
		list.add(m);
	}
	
	public void empty(){
		list = new ArrayList<MetaInfoTriple>();
	}
	
}
