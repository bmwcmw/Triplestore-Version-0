package localDBUtils;

import java.io.IOException;
import java.util.ArrayList;

public class InRamDBUtilsPOS implements DBImpl{
	private ArrayList<String> list1;
	private ArrayList<String> list2;
	
	public InRamDBUtilsPOS(){
		list1 = new ArrayList<String>();
		list2 = new ArrayList<String>();
	}

	public void closeAll() {}
	
	@Override
	public void put(String k, String v){
		list1.add(k);
		list2.add(v);
	}

	public Long fetchLoadedSize() {
		return (long)list1.size();
	}
	
	public ArrayList<String> getArr1() {
		return list1;
	}
	
	public ArrayList<String> getArr2() {
		return list2;
	}

	@Override
	public Long fetchIdByNode(String node) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fetchNodeById(Long index) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadIndexFromFile(String path) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(Long k, String v) {
		// TODO Auto-generated method stub
		
	}

}
