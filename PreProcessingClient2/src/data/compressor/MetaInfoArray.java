package data.compressor;

import java.util.ArrayList;

/**
 * Data structure for a list of Meta Info triple
 * @author CMWT420
 *
 */
public class MetaInfoArray {

	private ArrayList<MetaInfoQuadruple> list;
	
	public MetaInfoArray(){
		list = new ArrayList<MetaInfoQuadruple>();
	}
	
	public ArrayList<MetaInfoQuadruple> getList(){
		return list;
	}
	
	public void add(MetaInfoQuadruple m){
		list.add(m);
	}
	
	public void empty(){
		list = new ArrayList<MetaInfoQuadruple>();
	}
	
}
