import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BlobsTester {


	@Test
	static void test() throws NoSuchAlgorithmException, IOException {
		Blob test = new Blob("tst");
		Blob test2 = new Blob("tstst");
	}

}
