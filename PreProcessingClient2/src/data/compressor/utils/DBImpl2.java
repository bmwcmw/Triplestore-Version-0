package data.compressor.utils;

import java.io.IOException;

public abstract class DBImpl2 {
	
	abstract int fetchIndexSize();
	
	abstract Integer fetchIdByNode(String node);
	
	abstract void loadAuxIndexFromFile();
	
	abstract void compress(boolean writeIndex) throws IOException;
	
	protected abstract void writeMat(boolean writeIndex) throws IOException;
	
	protected abstract void writeAuxIndex() throws IOException;

	protected abstract void writeMeta() throws IOException;

}
