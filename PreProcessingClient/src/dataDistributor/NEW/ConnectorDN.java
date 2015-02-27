package dataDistributor.NEW;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import localIOUtils.IOUtils;

public class ConnectorDN {

    private static final int header_length = 8;

    private Socket socket;
    private BufferedReader socket_reader;
    private PrintWriter socket_writer;

    public ConnectorDN(String ipAddress, int port) throws IOException {
        try {
            InetAddress addr = InetAddress.getByName(ipAddress);
            socket = new Socket(addr, port);
            socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket_writer = new PrintWriter(new BufferedWriter(
            		new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
        } catch (Exception e) {
        	IOUtils.logLog("Connection : unable to open a connection on '" 
            		+ ipAddress + ":" + port + "' : " + e.getClass().getName() + ":" + e.getMessage());
            throw e;
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            IOUtils.logLog("Connection : close: unable to close the connection : " 
            		+ e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public void sendMessage(String message) throws IllegalArgumentException, NullPointerException {
        try {
            String header = Integer.toHexString(message.length());
            while (header.length() < header_length) {
                header = " " + header;
            }
            socket_writer.println(header + message);
        } catch (Exception e) {
        	IOUtils.logLog("Connection : sendMessage: error during the transfer of a message : " 
            		+ e.getClass().getName() + ":" + e.getMessage());
            throw e;
        }
    }

    public String receiveMessage() throws IOException {
        try {
            return socket_reader.readLine().substring(header_length);
        } catch (Exception e) {
        	IOUtils.logLog("Connection : receiveMessage : error during the message reception : " 
            		+ e.getClass().getName() + ":" + e.getMessage());
            throw e;
        }
    }

}
