package ctmNode;

import localIOUtils.IOUtils;

/**
 * Initial configuration settings
 * @author CEDAR
 *
 */
public class NodeListenerConfig {
	private static NodeListenerConfig config = null;
	
	// XXX SETUP : Base directory
	public final static String _workingDir = System.getProperty("user.dir");
	
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
}
