package localDBLoader;

import java.util.HashMap;

import localDBUtils.DBImpl;

public interface DBLoaderImpl {
	
	public static enum MODE {
		COMPRESSED, POS
	};
	
	HashMap<String, DBImpl> getDBList(String path, MODE mode) throws Exception;

}
