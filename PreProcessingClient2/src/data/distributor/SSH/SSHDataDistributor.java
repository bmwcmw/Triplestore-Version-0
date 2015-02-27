package data.distributor.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Send files via SSH
 * Using FileSender instead.
 * @author CMWT420
 *
 */
public class SSHDataDistributor {
	
	/**
	 * An alternative to send compressed files - via SSH/SFTP
	 * @param host IP address
	 * @param user Username for SSH
	 * @param pwd Password for SSH
	 * @param srcFilename Source file(path or only name - using default path) to send
	 * @param destFilename Source file(path or only name - using default path) to send
	 */
	public static void sendFileSFTP(String host, int port, String user, String pwd, 
			String srcFilename, String destFilename){
	    Session session = null;
	    Channel channel = null;
	    try {
	        JSch ssh = new JSch();
	        session = ssh.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
	        session.setPassword(pwd);
	        session.connect();
	        channel = session.openChannel("sftp");
	        channel.connect();
	        ChannelSftp sftp = (ChannelSftp) channel;
	        sftp.put(srcFilename, destFilename);
	    } catch (JSchException e) {
	        e.printStackTrace();
	    } catch (SftpException e) {
	        e.printStackTrace();
	    } finally {
	        if (channel != null) {
	            channel.disconnect();
	        }
	        if (session != null) {
	            session.disconnect();
	        }
	    }
	}
	
}
