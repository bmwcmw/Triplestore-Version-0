package dataCompressor;

/**
 * Data structure for a meta information (block mode)
 * Number of file, First S/O ID, Offset of first S/O.
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
