package dataCompressor;

public class UnusedDgapCompressor {
//	
//	/**
//	 * NOT USED
//	 * Initializes an matrix and fills in it
//	 */
//	public static int[][] matrix(ArrayList<Integer> subj, ArrayList<Integer> obj, 
//			DBImpl dbu, String opt) throws SQLException{
//		// initialized all 0
//		int[][] matrix = new int[dbu.fetchIndexSize()][dbu.fetchIndexSize()];
//		if(opt.equals("SO")){
//			// X=Object and Y=Subject
//			for (int i = 0; i < obj.size(); i++) {
//				matrix[obj.get(i)][subj.get(i)] += 1;
//			}
//		} else if (opt.equals("OS")) {
//			// X=Subject and Y=Object
//			for (int i = 0; i < obj.size(); i++) {
//				matrix[subj.get(i)][obj.get(i)] += 1;
//			}
//		}
//		return matrix;
//	}
//	
//	/**
//	 * NOT USED : int[][] too large
//	 */
//	public static void writeCompressedMatrixIndex(File f, int[][]mat, DBImpl dbu) 
//			throws IOException, SQLException{
//		Integer indexSize = dbu.fetchIndexSize();
//		String outLine;
//		int current;
//		int currentLength;
//		BufferedWriter fMat = new BufferedWriter(new OutputStreamWriter(
//				new FileOutputStream(f.getAbsolutePath(),true),"UTF-8"));
//		IOUtils.logLog("Writting in " + f.getName());
//		for (int i = 0; i < indexSize; i++) {
//			if (mat[0][i] == 0) {
//				outLine = "{[0]";
//				current = 0;
//			} else {
//				outLine = "{[1]";
//				current = 1;
//			}
//			currentLength = 1;
//
//			for (int j = 1; j < indexSize; j++) {
//				if (mat[j][i] == current) {
//					currentLength += 1;
//				} else {
//					outLine += "," + currentLength;
//					current = (current == 0) ? 1 : 0;
//					currentLength = 1;
//				}
//			}
//			outLine += "," + currentLength;
//			outLine += '}';
//
//			fMat.write(outLine);
//			fMat.newLine();
//		}
//		if (fMat != null) {
//			fMat.flush();
//			fMat.close();
//		}
//	}
}
