package dataDistributor.CEDAR;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;


@SuppressWarnings("unused")
public class JavaTest {

    public static final String myFolder = System.getProperty("user.dir");

    public static void main(String[] args) throws Exception {
        try {
            System.out.println(System.getProperty("java.version"));
            System.out.println(System.getProperty("java.vm.version"));
            System.out.println(System.getProperty("java.runtime.version"));
            System.out.println(System.getProperty("sun.arch.data.model"));
        } finally {
            System.out.println();
        }

        //String[] arg = {"134.214.213.155", "7474"};
        //String resp = RequestStringClient.run(arg);
        //System.out.println(resp);
        String directory_address = "127.0.0.1";
        int directory_port = 7474;
        String node_address = "127.0.0.1";
        int node_port = 7475;

        FileSenderCN fs = new FileSenderCN(node_address, node_port);
        fs.sendFile("D:\\the_hunger_game.txt", 20 * 1024);
        fs.close();
    }

    public static void runtimeParameters() {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        List<String> aList = bean.getInputArguments();

        for (int i = 0; i < aList.size(); i++) {
            System.out.println(aList.get(i));
        }
    }

}
