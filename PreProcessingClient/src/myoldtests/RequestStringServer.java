package myoldtests;

import java.net.*;
import java.io.*;

@SuppressWarnings("unused")
public class RequestStringServer {
	//Use c server instead
//	public static void main(String[] arges) throws IOException {
//		ServerSocket mySocket = null;
//		try {
//			int port = 8888;
//			mySocket = new ServerSocket(port);
//			Socket sk = mySocket.accept();
//			while(true){
//				BufferedReader in = new BufferedReader(new InputStreamReader(
//						sk.getInputStream()));
//				PrintWriter out = new PrintWriter(new BufferedWriter(
//						new OutputStreamWriter(sk.getOutputStream())), true);
//				System.out.println("Message of client : " + in.readLine());
//				out.println("Hi, I'm server using port : " + port);
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		} finally {
//			mySocket.close();
//		}
//	}
}
