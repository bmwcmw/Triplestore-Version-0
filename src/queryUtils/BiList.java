package queryUtils;

import java.util.ArrayList;
import java.util.List;

public class BiList {
	
	public class BiListPair{
		private Object O1;
		private Object O2;
		
		public BiListPair(Object o1, Object o2){
			O1 = o1;
			O2 = o2;
		}
		
		public Object elem1(){
			return O1;
		}
		
		public Object elem2(){
			return O2;
		}
	}
	
	private List<Object> list1;
	private List<Object> list2;
	
	public BiList(){
		list1 = new ArrayList<Object>();
		list2 = new ArrayList<Object>();
	}
	
	public void add(Object s1, Object s2){
		list1.add(s1);
		list2.add(s2);
	}
	
	public BiListPair get(int i){
		BiListPair resp = new BiListPair(list1.get(i), list2.get(i));
		return resp;
	}
	
	public int size(){
		return list1.size();
	}
}
