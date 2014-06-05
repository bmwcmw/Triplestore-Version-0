package queryUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import queryUtils.QueryUtils.VarType;

/**
 * This is used to store a graph of SPARQL queries with all sub-queries categorized
 * by their variable type.  
 * @author Cedar
 */
public class Graph {
	 private HashMap<VarType, ArrayList<Pattern>> graph;
	 
	 /**
	  * Inserts a pattern with its variable type into a Graph
	  * @param type : the type of pattern
	  * @param p : the pattern to insert
	  */
	 public void addPattern(VarType type, Pattern p){
		 ArrayList<Pattern> temp;
		 if ((temp = graph.get(type)) == null) {
			 ArrayList<Pattern> newArray = new ArrayList<Pattern>();
			 newArray.add(p);
			 graph.put(type, newArray);
		 } else {
			 temp.add(p); 
		 }
	 }
	 
	 /**
	  * Returns an ArrayList<Pattern> which represents a set of sub-queries
	  * with the same type of variable(s)
	  * @param type
	  * @param sorted
	  * @return a list of sub-queries
	  */
	 public ArrayList<Pattern> get(VarType type, boolean sorted){
		 ArrayList<Pattern> temp = graph.get(type);
		 if (sorted) sort(type, temp);
		 return temp;
	 }
	 
	 /**
	  * Sorts a list of patterns according to its variable type
	  * @param type
	  * @param arr
	  */
	 private void sort(VarType type, ArrayList<Pattern> arr){
		 switch(type){
		 	case S : 
		 		break;
		 	case P : 
		 		break;
		 	case O : 
		 		break;
		 	case SP : 
				Collections.sort(arr, new Comparator<Pattern>() {
					@Override
					public int compare(final Pattern p1, final Pattern p2) {
						return p1.getO().compareTo(p2.getO());
					}
				});
		 		break;
		 	case SO : 
				Collections.sort(arr, new Comparator<Pattern>() {
					@Override
					public int compare(final Pattern p1, final Pattern p2) {
						return p1.getP().compareTo(p2.getP());
					}
				});
		 		break;
		 	case PO : 
				Collections.sort(arr, new Comparator<Pattern>() {
					@Override
					public int compare(final Pattern p1, final Pattern p2) {
						return p1.getS().compareTo(p2.getS());
					}
				});
		 		break;
		 	case SPO : 
		 		break;
		 }
	 }
	 
}
