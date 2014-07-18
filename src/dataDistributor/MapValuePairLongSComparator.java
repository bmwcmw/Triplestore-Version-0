package dataDistributor;

import java.util.Comparator;
import java.util.Map;

import dataCleaner.CTMPairLong;

class MapValuePairLongSComparator implements Comparator<String> {

    Map<String, CTMPairLong> base;
    public MapValuePairLongSComparator(Map<String, CTMPairLong> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    @Override
    public int compare(String a, String b) {
        if (base.get(a).getSubject() >= base.get(b).getSubject()) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}