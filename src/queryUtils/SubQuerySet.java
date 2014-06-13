package queryUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import queryObjects.StringPattern;
import queryUtils.QueryUtils.VarType;

/**
 * This is used to store a graph of SPARQL queries with all sub-queries categorized
 * by their variable type.  
 * @author Cedar
 */
public class SubQuerySet {
	 private HashMap<VarType, ArrayList<StringPattern>> sets;
	 
	 /**
	  * Inserts a pattern with its variable type into a Graph
	  * @param type : the type of pattern
	  * @param p : the pattern to insert
	  */
	 public void addStringPattern(VarType type, StringPattern p){
		 ArrayList<StringPattern> temp;
		 if ((temp = sets.get(type)) == null) {
			 ArrayList<StringPattern> newArray = new ArrayList<StringPattern>();
			 newArray.add(p);
			 sets.put(type, newArray);
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
	 public ArrayList<StringPattern> get(VarType type, boolean sorted){
		 ArrayList<StringPattern> temp = sets.get(type);
		 if (sorted) sort(type, temp);
		 return temp;
	 }
	 
	 /**
	  * Sorts a list of patterns according to its variable type
	  * @param type
	  * @param arr
	  */
	 private void sort(VarType type, ArrayList<StringPattern> arr){
		 switch(type){
		 	case S : 
				Collections.sort(arr, new Comparator<StringPattern>() {
					@Override
					public int compare(final StringPattern p1, final StringPattern p2) {
						int res = p1.getP().compareTo(p2.getP());
						if(res==0){
							res = p1.getO().compareTo(p2.getO());
						} 
						return res;
					}
				});
		 		break;
		 	case P : 
				Collections.sort(arr, new Comparator<StringPattern>() {
					@Override
					public int compare(final StringPattern p1, final StringPattern p2) {
						int res = p1.getS().compareTo(p2.getS());
						if(res==0){
							res = p1.getO().compareTo(p2.getO());
						} 
						return res;
					}
				});
		 		break;
		 	case O : 
				Collections.sort(arr, new Comparator<StringPattern>() {
					@Override
					public int compare(final StringPattern p1, final StringPattern p2) {
						int res = p1.getS().compareTo(p2.getS());
						if(res==0){
							res = p1.getP().compareTo(p2.getP());
						} 
						return res;
					}
				});
		 		break;
		 	case SP : 
				Collections.sort(arr, new Comparator<StringPattern>() {
					@Override
					public int compare(final StringPattern p1, final StringPattern p2) {
						return p1.getO().compareTo(p2.getO());
					}
				});
		 		break;
		 	case SO : 
				Collections.sort(arr, new Comparator<StringPattern>() {
					@Override
					public int compare(final StringPattern p1, final StringPattern p2) {
						return p1.getP().compareTo(p2.getP());
					}
				});
		 		break;
		 	case PO : 
				Collections.sort(arr, new Comparator<StringPattern>() {
					@Override
					public int compare(final StringPattern p1, final StringPattern p2) {
						return p1.getS().compareTo(p2.getS());
					}
				});
		 		break;
		 	default : //case SPO : 
		 		break;
		 }
	 }
	 
}
