package server;

import localio.IOUtils;

/**
 * Initial configuration settings
 * @author CMWT420
 *
 */
public class NodeListenerConfig {
	private static NodeListenerConfig config = null;
	
	// XXX SETUP : Base directory
	private final static String _workingDir = System.getProperty("user.dir");
	
    private NodeListenerConfig() { 
		// XXX SETUP : Logs on/off
		IOUtils.setLogging(true, false);
    }
    
    public static synchronized NodeListenerConfig getInstance() {
        if (config == null) {
        	config = new NodeListenerConfig();
        }
        return config;
    }
    
    public static String getWorkingDir(){
    	return _workingDir;
    }
}
