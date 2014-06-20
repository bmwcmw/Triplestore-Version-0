package queryObjects;

import java.util.HashMap;

/**
 * This is used to store a graph of SPARQL queries with all sub-queries categorized
 * by their variable type.  
 * @author Cedar
 */
public class SubQuerySet {
	 private HashMap<Integer, StringPattern> set;
	 
	 public SubQuerySet(){
		 set = new HashMap<Integer, StringPattern>();
	 }
	 
	 /**
	  * Inserts a pattern into a Graph
	  * @param id : the unique id of pattern
	  * @param p : the pattern to insert
	  */
	 public void putStringPattern(Integer id, StringPattern p){
		 set.put(id, p);
	 }
	 
	 /**
	  * Returns the whole set which represents all sub-queries with the unique 
	  * identifiers
	  * @return the subset 
	  */
	 public HashMap<Integer, StringPattern> getAll(){
		 return set;
	 }
	 
	 /**
	  * Returns the size of the set
	  * @return the size
	  */
	 public int size(){
		 return set.size();
	 }
	 
	 /**
	  * Returns the sub-query having the unique identifier
	  * @return a sub-query
	  */
	 public StringPattern get(Integer i){
		 return set.get(i);
	 }
	 
//	 /**
//	  * Sorts a list of patterns according to its variable type
//	  * @param type
//	  * @param arr
//	  */
//	 private void sort(VarType type, ArrayList<StringPattern> arr){
//		 switch(type){
//		 	case S : 
//				Collections.sort(arr, new Comparator<StringPattern>() {
//					@Override
//					public int compare(final StringPattern p1, final StringPattern p2) {
//						int res = p1.getP().compareTo(p2.getP());
//						if(res==0){
//							res = p1.getO().compareTo(p2.getO());
//						} 
//						return res;
//					}
//				});
//		 		break;
//		 	case P : 
//				Collections.sort(arr, new Comparator<StringPattern>() {
//					@Override
//					public int compare(final StringPattern p1, final StringPattern p2) {
//						int res = p1.getS().compareTo(p2.getS());
//						if(res==0){
//							res = p1.getO().compareTo(p2.getO());
//						} 
//						return res;
//					}
//				});
//		 		break;
//		 	case O : 
//				Collections.sort(arr, new Comparator<StringPattern>() {
//					@Override
//					public int compare(final StringPattern p1, final StringPattern p2) {
//						int res = p1.getS().compareTo(p2.getS());
//						if(res==0){
//							res = p1.getP().compareTo(p2.getP());
//						} 
//						return res;
//					}
//				});
//		 		break;
//		 	case SP : 
//				Collections.sort(arr, new Comparator<StringPattern>() {
//					@Override
//					public int compare(final StringPattern p1, final StringPattern p2) {
//						return p1.getO().compareTo(p2.getO());
//					}
//				});
//		 		break;
//		 	case SO : 
//				Collections.sort(arr, new Comparator<StringPattern>() {
//					@Override
//					public int compare(final StringPattern p1, final StringPattern p2) {
//						return p1.getP().compareTo(p2.getP());
//					}
//				});
//		 		break;
//		 	case PO : 
//				Collections.sort(arr, new Comparator<StringPattern>() {
//					@Override
//					public int compare(final StringPattern p1, final StringPattern p2) {
//						return p1.getS().compareTo(p2.getS());
//					}
//				});
//		 		break;
//		 	default : //case SPO : 
//		 		break;
//		 }
//	 }
	 
}
