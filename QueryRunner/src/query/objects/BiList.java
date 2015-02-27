package query.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * A list containing pairs of objects(key, value)
 * @author CEDAR
 */
public class BiList<K, V> {
	
	private List<K> list1;
	private List<V> list2;
	
	public BiList(){
		list1 = new ArrayList<K>();
		list2 = new ArrayList<V>();
	}
	
	/**
	 * Adds two Objects = a pair
	 * @param s1
	 * @param s2
	 */
	public void add(K s1, V s2){
		list1.add(s1);
		list2.add(s2);
	}
	
	/**
	 * Gets the i-th pair
	 * @param i
	 * @return BiListPair
	 */
	public BiPair<K,V> get(int i){
		BiPair<K,V> resp = new BiPair(list1.get(i), list2.get(i));
		return resp;
	}
	
	public int size(){
		return list1.size();
	}
}
