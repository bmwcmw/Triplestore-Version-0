package myoldtests;

import java.io.File;
import java.nio.charset.Charset;

import data.distributor.light.ConnectorDN;
import localIOUtils.IOUtils;

@SuppressWarnings("unused")
public class RequestStringClient {

    public static String run(String[] arges) {
        try {
            //134.214.142.58
            /*InetAddress addr = InetAddress.getByName("134.214.142.58");
             Socket sk = new Socket(addr, 7474);
             BufferedReader in = new BufferedReader(new InputStreamReader(
             sk.getInputStream()));
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sk.getOutputStream())), true);
             out.println("HelloHelloHelloHelloHelloHelloHelloHelloHelloHello");
             System.out.println(in.readLine());*/
            ConnectorDN directory_node = new ConnectorDN(arges[0], Integer.parseInt(arges[1]));
//            String filePath = ".." + File.separator + 
//  				"CtmRdf" + File.separator + "__n3(26M)" + File.separator +"University1_2.n3";
            
            directory_node.sendMessage("DATA:N<a.in>,D<XXXX>;");
//            directory_node.sendMessage(IOUtils.fileToString(filePath, Charset.defaultCharset()));
            return directory_node.receiveMessage();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
