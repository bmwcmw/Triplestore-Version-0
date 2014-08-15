package dataCompressor;

/**
 * Data structure for a meta information (block mode)
 * Number of file, First occured S/O's ID, Offset of O/S (0 or last O/S of last file).
 * @author Cedar
 *
 */
public class MetaInfoTriple {

	public final int nFile;
	public final Long id;
	public final Long offset;
	
	public MetaInfoTriple(int n, long i, long o){
		nFile = n;
		id = i;
		offset = o;
	}
	
}
