package dataDistributor;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Use FileSender instead.
 * @author Cedar
 *
 */
public class DataDistributor {
//	
//	/**
//	 * An alternative to send compressed files - via socket, to a C++ server(Boost)
//	 * @param host IP address
//	 * @param port Server's port number
//	 * @param srcFilePath Path of the source file to send
//	 * @param bufferSize in Mb
//	 * @throws IOException 
//	 */
//	public static void sendFileSocket(String host, int port, String srcFilePath, int bufferSize) 
//			throws IOException{
//		File file = new File(srcFilePath);
//		String inFileName = file.getName();
//		BufferedReader reader = null;
//		long lineNb = 0;
//		try{
//			DNConnection calculateNode = new DNConnection(host, port);
//			reader = new BufferedReader(new FileReader(srcFilePath));
//			String buffer = "DATA:<"+inFileName+">";
//			String respStr;
//			String line;
//			int freeBufferSize = bufferSize*1024*1024;
//			while ((line = reader.readLine()) != null) {
//				lineNb ++;
//				if(freeBufferSize - line.getBytes().length < bufferSize*1024*1024){
//					calculateNode.sendMessage("HELLO");
//					respStr = calculateNode.receiveMessage();
//					if(!respStr.equals("DATA;ACK")){
//						IOUtils.logLog("Error sending file via socket : " + srcFilePath + " line " + lineNb);
//						throw new IOException("ERROR : Server returns " + respStr);
//					}
//					buffer = "DATA:";
//					freeBufferSize = bufferSize*1024*1024;
//				}
//				freeBufferSize = freeBufferSize - line.getBytes().length;
//				buffer += line;
//			}
//			buffer += ";";
//			calculateNode.sendMessage(buffer);
//			respStr = calculateNode.receiveMessage();
//			if(!respStr.equals("DATA:ACK")){
//				IOUtils.logLog("Error sending file via socket : " + srcFilePath + " line " + lineNb);
//				throw new IOException("ERROR : Server returns " + respStr);
//			}
//			calculateNode.close();
//		} catch (IOException e) {
//			IOUtils.logLog("Error sending file via socket : " + srcFilePath + " line " + lineNb);
//			throw e;
//		} finally {
//			reader.close();
//		}
//		
//	}
	
	/**
	 * An alternative to send compressed files - via SSH/SFTP
	 * @param host IP address
	 * @param user Username for SSH
	 * @param pwd Password for SSH
	 * @param srcFilename Source file(path or only name - using default path) to send
	 * @param destFilename Source file(path or only name - using default path) to send
	 */
	public static void sendFileSFTP(String host, String user, String pwd, 
			String srcFilename, String destFilename){
	    Session session = null;
	    Channel channel = null;
	    try {
	        JSch ssh = new JSch();
	        session = ssh.getSession(user, host, 22);
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
