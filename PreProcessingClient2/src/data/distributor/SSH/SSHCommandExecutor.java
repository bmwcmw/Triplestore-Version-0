package data.distributor.ssh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHCommandExecutor {
	
	public static void execute(String host, String user, String pwd, String command) {
	    try {
	        JSch jsch = new JSch();
	        Session session = jsch.getSession(user, host, 22);
	        session.setConfig("StrictHostKeyChecking", "no");
	        session.setPassword(pwd);
	        session.connect();

	        ChannelExec channelExec = (ChannelExec)session.openChannel("exec");

	        InputStream in = channelExec.getInputStream();

	        channelExec.setCommand(command);
	        channelExec.connect();

	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        String line;
	        int index = 0;

	        while ((line = reader.readLine()) != null)
	        {
	            System.out.println(++index + " : " + line);
	        }

	        int exitStatus = channelExec.getExitStatus();
	        channelExec.disconnect();
	        session.disconnect();
	        if(exitStatus < 0){
	            System.out.println("Done without exit status.");
	        }
	        else if(exitStatus > 0){
	            System.out.println("Done but with error.");
	        }
	        else{
	            System.out.println("Done.");
	        }
	    }
	    catch(Exception e)
	    {
	        System.err.println("Error: " + e);
	    }
	}
}
