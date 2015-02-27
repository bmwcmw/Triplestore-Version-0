package data.distributor;

import java.util.Comparator;
import java.util.Map;

import data.cleaner.RDFPairLong;

class MapValuePairLongOComparator implements Comparator<String> {

    Map<String, RDFPairLong> base;
    public MapValuePairLongOComparator(Map<String, RDFPairLong> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    @Override
    public int compare(String a, String b) {
        if (base.get(a).getObject() >= base.get(b).getObject()) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}