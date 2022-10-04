import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Commit {
	String p; 
	String c;
	String date;
	String name;
	String summary;
	Tree tree;
	public Commit(String parent, String child, String author, String description) {//Also need to get rid of parent?, not needed
		p = parent;
		c = child;
		summary = description;
		name = author;
		ArrayList<String> list = new ArrayList <String> ();
		String s = "";
		
		if(p != null) {
			Path path = Paths.get("./objects/" + p);
			try {
				s = Files.readString(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add("tree : " + s.substring(0,32));//Fix this
		}
		
		String sub = "";
		String blobname = "";
		
		Path p = Paths.get("index.txt");
		try {
			s = Files.readString(p);
			
			while (s.indexOf(":") != -1) {
				blobname = s.substring(0,s.indexOf(":")-1);
				sub = s.substring(s.indexOf(":")+2, s.indexOf(":")+34);
				s = s.substring(s.indexOf(":")+35);
				list.add("blob : " + sub + " " + blobname);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tree = new Tree (list);
	}
	public Tree getTree() {
		return tree;
	}
	public static String encrypt(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger num = new BigInteger(1, messageDigest);
			String hex = num.toString(16);
			while (hex.length() < 40) {
				hex = "0" + hex;
			}
			return hex;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	public String generateSHA1() {
		return encrypt(summary + getDate() + name + p);
	}
	public String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		date = dtf.format(now);  
		return date;
	}
	public void writeFile() throws FileNotFoundException {
		File f = new File("objects", generateSHA1());
		PrintWriter pw = new PrintWriter(f);
		pw.println(tree.generateSHA1());
		pw.println(p);
		pw.println(c);
		pw.println(name);
		pw.println(getDate());
		pw.println(summary);
		pw.close();
	}
	
	
}
