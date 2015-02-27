package constants;

/**
 * Constants for all optional preprocessing modules.
 * @author CMWT420
 */
public class AppConstants {
	/* Options */
	public final static int APPCONVERTER = 101;
	public final static int APPREADERPS = 102;
	public final static int APPREADERPOS = 103;
	
	public final static int APPCOMPRESS = 104;
	public final static int APPCOMPRESS_INRAM = 1041;
//	public final static int APPCOMPRESS_REDIS = 1042;
//	public final static int APPCOMPRESS_MONET = 1043;
//	public final static int APPCOMPRESS_POSTGRES = 1044;
//	public final static int APPCOMPRESS_ORACLE = 1045;
//	public final static int APPCOMPRESS_MYSQL = 1046;
//	public final static int APPCOMPRESS_MONGO = 1047;
	
//	public final static int APPPRECOMPARE = 105;
//	public final static int APPPRECOMPARE_JAVA = 1051;
//	public final static int APPPRECOMPARE_PERL = 1052;
	
	public final static int APPCOMPARE = 106;
//	public final static int APPCOMPARE_JAVA_INRAM = 1061;
	public final static int APPCOMPARE_JAVA = 1062;
	public final static int APPCOMPARE_GNU = 1063;
//	public final static int APPCOMPARE_PERL = 1064;
	
	public final static int APPDISTRIBUTE = 107;
	public final static int APPDISTRIBUTE_LIGHT = 1071;
	public final static int APPDISTRIBUTE_HDFS = 1072;
	
	public final static int APPINDICATORS = 10701;
	public final static int APPINDICATORO = 10702;
	public final static int APPINDICATORSO = 10703;
	
	public final static int APPEXIT = -1;
	public final static int APPEMPTY = 10000;
	
	/* File extension */
	public final static String SOMatrixExt = ".SO";
	public final static String OSMatrixExt = ".OS";
	
	public final static String IndexExt = ".index";
	public final static String MetadataExt = ".MD";
	
	public final static String IndexSExt = ".indexS";
	public final static String IndexOExt = ".indexO";
	public final static String MetadataSExt = ".MDS";
	public final static String MetadataOExt = ".MDO";
	
	public final static String SOSortedExt = ".sortedSO";
	public final static String OSSortedExt = ".sortedOS";
	
	public final static String SArrayExt = ".S";
	public final static String OArrayExt = ".O";
	
	/* Others */
	public final static char delimiter = ' ';
	
	/* Type */
	public final static String rdfTypeHeader = "rdf-type_";
	
}
