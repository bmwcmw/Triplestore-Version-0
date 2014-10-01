package dataCompressor;

import java.util.Comparator;

public class SOLongPairOComparator implements Comparator<SOLongPair>{
	
	@Override
    public int compare(SOLongPair p1, SOLongPair p2){
        return p1.O.compareTo(p2.O);
    }
	
}