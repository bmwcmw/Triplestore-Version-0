package ctmSPARQL;

/**
 * Data structure for the meta information in one block (block mode) :
 * <br>
 * - File ID (last number)
 * <br>
 * - Offset (ID) of O/S (0 or last O/S of last file)
 * <br>
 * - Offset (line number from 1st cut)
 * @author Cedar
 *
 */
public class MetaInfoTriple {
	public final int nFile;
	public final Long offsetID;
	public final int offsetLine;
	
	public MetaInfoTriple(int n, long o, int ol){
		nFile = n;
		offsetID = o;
		offsetLine = ol;
	}
	
}
