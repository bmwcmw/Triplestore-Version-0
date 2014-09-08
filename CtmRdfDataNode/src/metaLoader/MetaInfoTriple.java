package metaLoader;

/**
 * Data structure for the meta information in one block (block mode) :
 * <br>
 * - First occured S/O's ID
 * <br>
 * - Offset (ID) of O/S (0 or last O/S of last file)
 * <br>
 * - Offset (line number from 1st cut)
 * @author Cedar
 *
 */
public class MetaInfoTriple {
	public final Long id;
	public final Long offsetID;
	public final int offsetLine;
	
	public MetaInfoTriple(long i, long o, int ol){
		id = i;
		offsetID = o;
		offsetLine = ol;
	}
	
}
