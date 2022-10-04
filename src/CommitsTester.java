import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

class CommitsTester {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	static void test() throws NoSuchAlgorithmException, IOException {
		Index test = new Index();
		test.init();
		test.add("tstst");
		test.add("tsts");
		
		Commit test1 = new Commit("Jack", "First");
		test1.writeFile();
		
		test.add("tst");
		
		Commit test2 = new Commit("Jack", "Second");
		test2.writeFile();
		
		test.add("tstst - Copy");
		test.add("tstst - Copy (2)");
		test.add("tstst - Copy (3)");
		test.add("tstst - Copy (4)");
		test.add("tstst - Copy (5)");
		
		Commit test3 = new Commit("Jack", "Third");
		test3.writeFile();
		
		test.add("tstst - Copy (6)");
		test.add("tstst - Copy (7)");
		
		Commit test4 = new Commit("Jack", "Fourth");
		test4.writeFile();
		
		test.delete("tstst - Copy (2)");
		test.add("tstst - Copy (8)");
		
		Commit test5 = new Commit("Jack", "Fifth");
		test5.writeFile();
	}

}
