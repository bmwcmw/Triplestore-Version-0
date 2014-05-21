package dataCompressor;

import java.util.Comparator;

public class ComparatorS implements Comparator<SOIntegerPair>{
	
	@Override
    public int compare(SOIntegerPair p1, SOIntegerPair p2){
        return p1.S.compareTo(p2.S);
    }
	
}