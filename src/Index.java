import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Index {
	PrintWriter pw;
	private static HashMap<String, String> files = new HashMap<String, String>();
	File index;
	File head;
	public Index() throws FileNotFoundException {
		
	}
	public void init() {
		File theindex = new File("index");
		index = theindex;
		
		Path p = Paths.get("HEAD");//gets the path to the file
	    try {
	        Files.writeString(p, "PLACEHOLDER", StandardCharsets.ISO_8859_1); //makes the index file
	        Files.writeString(p, "", StandardCharsets.ISO_8859_1); //makes the index file
	    } catch (IOException e) {
	        e.printStackTrace();
	    }	
	    
		
		File theDir = new File("objects");
		if (!theDir.exists()){
		    theDir.mkdirs();
		}
	}
	public void editIndex() throws FileNotFoundException {
		pw = new PrintWriter(index);
		for(String s : files.keySet()) {
			pw.println(s + " : " + files.get(s));
		}
		pw.close();
	}
	public void add(String FileName) throws IOException, NoSuchAlgorithmException {
		Blob blob = new Blob(FileName);
		files.put(FileName, blob.getSHA1());
		editIndex();
	}
	public void remove(String FileName) throws FileNotFoundException {
		File removee = new File("objects", files.get(FileName));
		files.remove(FileName);
		removee.delete();
		editIndex();
	}
	
	public void delete (String FileName) throws NoSuchAlgorithmException, IOException {
		Blob blob = new Blob(FileName);
		files.put("*deleted* " + FileName, blob.getSHA1());
		editIndex();
	}
	
	public void edit (String FileName) throws NoSuchAlgorithmException, IOException {
		Blob blob = new Blob(FileName);
		files.put("*edited* " + FileName, blob.getSHA1());
		editIndex();
	}
	
	public static void empty () {
		files = new HashMap <String, String>();
	}

}