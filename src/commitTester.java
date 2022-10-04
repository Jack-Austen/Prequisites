import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class commitTester {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		Index test = new Index();
		test.init();
		test.add("tstst");
		test.add("tsts");
		test.add("tst");
		
		Commit test1 = new Commit(null, "Jack", "First");
		test1.writeFile();
		
		
		//Commit test2 = new Commit();
		
		
		//test1.writeFile();
		//test2.writeFile();
	}

} 
