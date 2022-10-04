import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class IndexTester {
	public static void main (String [] args) throws IOException, NoSuchAlgorithmException {
		Index test = new Index();
		System.out.println(System.getProperty("user.dir"));
		test.init();
		test.add("tst");
		test.add("tsts");
		test.remove("tst");
		test.remove("tstst");
	}
}