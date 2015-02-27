package data.compressor;

import java.util.Comparator;

public class SOLongPairSComparator implements Comparator<SOLongPair>{
	
	@Override
    public int compare(SOLongPair p1, SOLongPair p2){
        return p1.S.compareTo(p2.S);
    }
	
}