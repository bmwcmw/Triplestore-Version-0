package myoldtests;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

@SuppressWarnings("unused")
public class Part1_2JavaTest {
	public static final String myFolder = System.getProperty("user.dir");
	
	public static void main(String[] args) throws Exception {
		try{
			System.out.println(System.getProperty("java.version"));
			System.out.println(System.getProperty("java.vm.version"));
			System.out.println(System.getProperty("java.runtime.version"));
			System.out.println(System.getProperty("sun.arch.data.model"));
			System.out.println("Integer.MAX_VALUE : " + Integer.MAX_VALUE);
			System.out.println("Long.MAX_VALUE : " + Long.MAX_VALUE);
			System.out.println("Double.MAX_VALUE : " + Double.MAX_VALUE);
		} finally {
			System.out.println();
		}
		
		System.out.println(Long.valueOf("1234567890123456789"));
		
		//MapSetInLoop.HashMapInLoop();
		
		String test = "testtest";
		System.out.println(test.subSequence(0, test.length()-1));
		
		/*Distribution*/
//		SSHExecutor.execute("192.168.56.101", "cmw", "123xsd", "pwd");
//		DataDistributor.sendFileSFTP("192.168.56.101", 22, "cmw", "123xsd", "2014-05-15.log", "2014-05-15.log");
//		SSHExecutor.execute("192.168.56.101", "cmw", "123xsd", 
//				"/home/cmw/Bureau/hadoop-1.2.1/bin/hadoop dfs "
//				+ "-copyFromLocal 2014-05-15.log hdfs://localhost:9000/user/cmw/tmp");
//		SSHExecutor.execute("192.168.56.101", "cmw", "123xsd", 
//				"/home/cmw/Bureau/hadoop-1.2.1/bin/hadoop dfs "
//				+ "-ls hdfs://localhost:9000/user/cmw/tmp");
		
//		ArrayList<ArrayList<File>> groups = CTMServer.groupBySimilarities(
//				myFolder + File.separator + ".." + File.separator + "CtmRdf" + File.separator + "_indicator", 
//				myFolder + File.separator + ".." + File.separator + "CtmRdf" + File.separator + "_compressed", false);
		
//	AboutArray.ArrayListRemoveTest();
		
		/*Comparison*/
//		long start, end, duration;
//		start = System.currentTimeMillis();
//		JavaComparator jc = new JavaComparator();
//		System.out.println(jc.compareTwoPredicates(new File(myFolder+File.separator+"__comm"+File.separator+"a1.in"), 
//				new File(myFolder+File.separator+"__comm"+File.separator+"a2.in")) );
//		end = System.currentTimeMillis();
//		duration = end - start;
//		System.out.println("JAVA Method uses "+duration);
//		start = System.currentTimeMillis();
//		GnuComparator gc = new GnuComparator();
//		System.out.println(gc.execute(myFolder+File.separator+"__comm"+File.separator+"1.in", 
//				myFolder+File.separator+"__comm"+File.separator+"2.in") );
//		end = System.currentTimeMillis();
//		duration = end - start;
//		System.out.println("PERL Method uses "+duration);
//		start = System.currentTimeMillis();
//		PerlComparator pc = new PerlComparator();
//		System.out.println(pc.execute(myFolder+File.separator+"__comm"+File.separator+"1.in", 
//				myFolder+File.separator+"__comm"+File.separator+"2.in") );
//		end = System.currentTimeMillis();
//		duration = end - start;
//		System.out.println("PERL Method uses "+duration);
		
//		String[] arg = {"134.214.213.155","7474"};
//		String resp = RequestStringClient.run(arg);
//		JSONParser parser = new JSONParser();
//		JSONArray jsonResp = (JSONArray) parser.parse(resp);
//		JSONObject jsonObj = (JSONObject) jsonResp.get(0);
//		System.out.println(resp);
//		CNConnection.test();
		
//		JedisClient.testPipeline();
//		JedisClient.test();
		
//		AboutArray.IntMatTest();
		
//		JSON.SimuSimilarity();

		//CommunicationManager.Client();
//		File f1 = new File(myFolder + File.separator + "a.in");
//		File f2 = new File(myFolder + File.separator + "-takesCourse.in");
//		System.out.println(Comparator.compareTwoPredicates(f1, f2, Comparator.S));
		
		//IOTests.WriterTest();
		//IOTests.N3ReaderTest();
		
		//IOThreadTests.IOThreadTest0();
		
//		int test = 0;
//		if( (test++) !=0 ){}
//		System.out.println(test);
		
//		int i=0;
//		while(i<30){
//			i++;
//			if(i%5==0) continue;
//			System.out.println(i);
//		}
		
//		DBUtils dbu;
//		Integer i;
//		String n;
//		
//		System.out.println("MonetDBUtils");
//		dbu = new MonetDBUtils();
//		n = "coco";
//		i = dbu.fetchIdByNode(n);
//		if(i != null){
//			System.out.println(n+" found : id="+i);
//		} else {
//			dbu.insertNode(n);
//			i = dbu.fetchIdByNode(n);
//			System.out.println(n+" not found : new id="+i);
//		}
//		System.out.println("Fetch node by id : "+dbu.fetchNodeById(i));
//		System.out.println("Current size : " + dbu.fetchTableSize());
//		
//		dbu.closeAll();
//
//		System.out.println("PostgreSQLUtils");
//		dbu = new PostgreSQLUtils();
//		n = "test";
//		i = dbu.fetchIdByNode(n);
//		if(i != null){
//			System.out.println(n+" found : id="+i);
//		} else {
//			i = dbu.insertNode(n);
//			System.out.println(n+" not found : new id="+i);
//		}
//		System.out.println("Fetch node by id : "+dbu.fetchNodeById(i));
//		System.out.println("Current size : " + dbu.fetchTableSize());
//		dbu.closeAll();
	}

	public static void runtimeParameters() {
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		List<String> aList = bean.getInputArguments();

		for (int i = 0; i < aList.size(); i++) {
			System.out.println(aList.get(i));
		}
	}

}
