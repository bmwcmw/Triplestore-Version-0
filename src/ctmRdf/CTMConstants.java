package ctmRdf;

/**
 * Constants for all optional modules.
 * @author CEDAR
 */
public class CTMConstants {
	public final static int CTMCONVERTER = 101;
	public final static int CTMREADERPS = 102;
	public final static int CTMREADERPOS = 103;
	
	public final static int CTMCOMPRESS = 104;
	public final static int CTMCOMPRESS_INRAM = 1041;
	public final static int CTMCOMPRESS_REDIS = 1042;
	public final static int CTMCOMPRESS_MONET = 1043;
	public final static int CTMCOMPRESS_POSTGRES = 1044;
	public final static int CTMCOMPRESS_ORACLE = 1045;
	public final static int CTMCOMPRESS_MYSQL = 1046;
	public final static int CTMCOMPRESS_MONGO = 1047;
	
	public final static int CTMPRECOMPARE = 105;
	public final static int CTMPRECOMPARE_JAVA = 1051;
	public final static int CTMPRECOMPARE_PERL = 1052;
	
	public final static int CTMCOMPARE = 106;
	public final static int CTMCOMPARE_JAVA_INRAM = 1061;
	public final static int CTMCOMPARE_JAVA = 1062;
	public final static int CTMCOMPARE_GNU = 1063;
	public final static int CTMCOMPARE_PERL = 1064;
	
	public final static int CTMDISTRIBUTE = 107;
	public final static int CTMDISTRIBUTE_CEDAR = 1071;
	public final static int CTMDISTRIBUTE_HDFS = 1072;
	
	public final static int CTMINDICATORS = 10701;
	public final static int CTMINDICATORO = 10702;
	public final static int CTMINDICATORSO = 10703;
	
	public final static int CTMEXIT = -1;
	public final static int CTMEMPTY = 10000;
}
