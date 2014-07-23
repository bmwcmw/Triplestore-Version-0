package dataDistributor.CEDAR;

/**
 * Pair of two strings
 * @author Cedar
 */
public class DestInfo{
	public String addr;
	public int port;
	public int size;
	
	public DestInfo(String address, int portnumber, int chunksize){
		addr = address;
		port = portnumber;
		size = chunksize;
	}
}