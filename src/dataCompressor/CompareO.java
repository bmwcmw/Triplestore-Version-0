package dataCompressor;

import java.util.Comparator;

public class CompareO implements Comparator<SOIntegerPair>{
	
	@Override
    public int compare(SOIntegerPair p1, SOIntegerPair p2){
        return p1.O.compareTo(p2.O);
    }
	
}