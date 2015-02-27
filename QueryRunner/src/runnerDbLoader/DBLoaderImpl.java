package runnerDbLoader;

import java.util.HashMap;

import runnerDbUtils.DBImpl;

/**
 * DB loader interface
 * @author CMWT420
 *
 */
public interface DBLoaderImpl {
	
	public static enum MODE {
		COMPRESSED, POS
	};
	
	HashMap<String, DBImpl> getDBList(String path, MODE mode) throws Exception;

}
