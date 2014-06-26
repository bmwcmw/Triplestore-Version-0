package dataCompressor;

import java.util.Comparator;

public class CompareS implements Comparator<SOLongPair>{
	
	@Override
    public int compare(SOLongPair p1, SOLongPair p2){
        return p1.S.compareTo(p2.S);
    }
	
}