package dataDistributor.NEW;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Deprecated class
 * @author CMWT420
 *
 */
public class ConnectorCN {

    private static final int header_length = 8;
    private static final int size_packet = 50 * 1024 * 1024;
    private static final String data_separator = "||-||";

    private Socket socket;
    private BufferedReader socket_reader;
    private PrintWriter socket_writer;

    public ConnectorCN(String ipAddress, int port) throws IOException {
        try {
            InetAddress addr = InetAddress.getByName(ipAddress);
            socket = new Socket(addr, port);
            socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket_writer = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
        } catch (Exception e) {
            System.err.println("Connection : unable to open a connection on '"
                    + ipAddress + ":" + port + "' : " + e.getClass().getName() + ":" + e.getMessage());
            throw e;
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            System.err.println("Connection : close: unable to close the connection : "
                    + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public void sendData(String filename, String data) throws IllegalArgumentException, NullPointerException, IOException {
        if (data.length() > size_packet) {
            sendDataByPart(filename, data);
        } else {
            sendDataOneBlock(filename, data);
        }
    }

    private void sendDataOneBlock(String filename, String data) throws IllegalArgumentException, NullPointerException, IOException {
        try {
            String header = Integer.toHexString(data.length());
            while (header.length() < header_length) {
                header = " " + header;
            }
            data = "DATA:N<" + filename + ">" + data_separator + data;
            socket_writer.print(header + data);
            socket_writer.flush();
            String answer = receiveMessage();
            if (!answer.equals("DATA:ACK;")) {
                throw new IllegalArgumentException("Error during transmission");
            }
        } catch (Exception e) {
            System.err.println("Connection : sendMessage: error during the transfer of a message : "
                    + e.getClass().getName() + ":" + e.getMessage());
            throw e;
        }
    }

    private void sendDataByPart(String filename, String data) throws IllegalArgumentException, NullPointerException, IOException {
        try {
            int position = 0;
            boolean finish = false;
            for (int i = 0; !finish; i++) {
                String message = "";
                if (position + size_packet < data.length()) {
                    message = "PDTA:N<" + filename + ">,P<" + i + ">" + data_separator + data.substring(position, position + size_packet);
                    position += size_packet;
                } else {
                    message = "PDTA:N<" + filename + ">,P<" + i + ">LAST" + data_separator + data.substring(position, data.length());
                    finish = true;
                }
                String header = Integer.toHexString(message.length());
                while (header.length() < header_length) {
                    header = " " + header;
                }
                socket_writer.print(header + message);
                socket_writer.flush();
                String answer = receiveMessage();
                if (!answer.equals("DATA:ACK<" + i + ">;")) {

                    System.err.println("problem occurs during transmission on i=" + i + ":" + answer);
                    throw new IllegalArgumentException("Error during transmission of i(" + i + ")");
                }
            }
        } catch (Exception e) {
            System.err.println("Connection : sendMessage: error during the transfer of a message : "
                    + e.getClass().getName() + ":" + e.getMessage());
            throw e;
        }
    }

    public String receiveMessage() throws IOException {
        try {
            return socket_reader.readLine().substring(header_length);
        } catch (Exception e) {
            System.err.println("Connection : receiveMessage : error during the message reception : "
                    + e.getClass().getName() + ":" + e.getMessage());
            throw e;
        }
    }

}
