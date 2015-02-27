package data.comparator2;

import java.io.File;


/**
 * Pair of two files(with separated S and O) for comparison purpose
 * @author CMWT420
 */
public class FilePair{
	public File f1S;
	public File f1O;
	public File f2S;
	public File f2O;
	public FilePair(File file1S, File file1O, File file2S, File file2O){
		f1S = file1S;
		f1O = file1O;
		f2S = file2S;
		f2O = file2O;
	}
}