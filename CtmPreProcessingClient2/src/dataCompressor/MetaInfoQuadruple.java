package dataCompressor;

/**
 * Data structure for meta information (block mode) :
 * <br>
 * - File ID (last number)
 * <br>
 * - First occured S/O's ID
 * <br>
 * - Offset (ID) of O/S (0 or last O/S of last file)
 * <br>
 * - Offset (line number from 1st cut)
 * @author Cedar
 *
 */
public class MetaInfoQuadruple {

	public final int nFile;
	public final Long id;
	public final Long offsetID;
	public final int offsetLine;
	
	public MetaInfoQuadruple(int n, long i, long o, int ol){
		nFile = n;
		id = i;
		offsetID = o;
		offsetLine = ol;
	}
	
}
