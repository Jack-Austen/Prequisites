import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class commitTester {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		Index test = new Index();
		test.init();
		test.add("tstst");
		test.add("tsts");
		
		Commit test1 = new Commit(null, "Jack", "First");
		test1.writeFile();
		
		test.add("tst");
		
		Commit test2 = new Commit(test1.getFileName(), "Jack", "Second");
		test2.writeFile();
	}

} 
