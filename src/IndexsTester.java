import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IndexsTester {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	static void test() throws NoSuchAlgorithmException, IOException {
		Index test = new Index();
		System.out.println(System.getProperty("user.dir"));
		test.init();
		test.add("tst");
		test.add("tsts");
		test.remove("tst");
		test.delete("tsts");
		test.add("tstst");
		test.edit("tstst");
	}

}
