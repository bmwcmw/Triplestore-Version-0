package query.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import query.utils.QueryUtils.VarType;

/**
 * This is used to store a result set of SPARQL queries, categorized
 * by sub-queries and their variable type.  
 * @author CMWT420
 */
public class ResultSet {
	 private HashMap<VarType, ArrayList<StringTriple>> graph;
	 
	 /**
	  * Inserts a pattern with its variable type into a Graph
	  * @param type : the type of pattern
	  * @param p : the pattern to insert
	  */
	 public void addPattern(VarType type, StringTriple p){
		 ArrayList<StringTriple> temp;
		 if ((temp = graph.get(type)) == null) {
			 ArrayList<StringTriple> newArray = new ArrayList<StringTriple>();
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
	 public ArrayList<StringTriple> get(VarType type, boolean sorted){
		 ArrayList<StringTriple> temp = graph.get(type);
		 if (sorted) sort(type, temp);
		 return temp;
	 }
	 
	 /**
	  * Sorts a list of patterns according to its variable type
	  * @param type
	  * @param arr
	  */
	 private void sort(VarType type, ArrayList<StringTriple> arr){
		 switch(type){
		 	case S : 
				Collections.sort(arr, new Comparator<StringTriple>() {
					@Override
					public int compare(final StringTriple p1, final StringTriple p2) {
						int res = p1.getP().compareTo(p2.getP());
						if(res==0){
							res = p1.getO().compareTo(p2.getO());
						} 
						return res;
					}
				});
		 		break;
		 	case P : 
				Collections.sort(arr, new Comparator<StringTriple>() {
					@Override
					public int compare(final StringTriple p1, final StringTriple p2) {
						int res = p1.getS().compareTo(p2.getS());
						if(res==0){
							res = p1.getO().compareTo(p2.getO());
						} 
						return res;
					}
				});
		 		break;
		 	case O : 
				Collections.sort(arr, new Comparator<StringTriple>() {
					@Override
					public int compare(final StringTriple p1, final StringTriple p2) {
						int res = p1.getS().compareTo(p2.getS());
						if(res==0){
							res = p1.getP().compareTo(p2.getP());
						} 
						return res;
					}
				});
		 		break;
		 	case SP : 
				Collections.sort(arr, new Comparator<StringTriple>() {
					@Override
					public int compare(final StringTriple p1, final StringTriple p2) {
						return p1.getO().compareTo(p2.getO());
					}
				});
		 		break;
		 	case SO : 
				Collections.sort(arr, new Comparator<StringTriple>() {
					@Override
					public int compare(final StringTriple p1, final StringTriple p2) {
						return p1.getP().compareTo(p2.getP());
					}
				});
		 		break;
		 	case PO : 
				Collections.sort(arr, new Comparator<StringTriple>() {
					@Override
					public int compare(final StringTriple p1, final StringTriple p2) {
						return p1.getS().compareTo(p2.getS());
					}
				});
		 		break;
		 	default : //case SPO : 
		 		break;
		 }
	 }
	 
}
