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
	Commit p; 
	String c;
	String date;
	String name;
	String summary;
	Tree tree;
	public Commit(Commit parent, String author, String description) {//Also need to get rid of parent?, not needed
		p = parent;
		c="";
		summary = description;
		name = author;
		date = getDate();
		ArrayList<String> list = new ArrayList <String> ();
		String s = "";
		
		if(p != null) {
			Path path = Paths.get("./objects/" + p.generateSHA1());
			try {
				s = Files.readString(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add("tree : " + s.substring(0,40));//Fix this
		}
		
		String sub = "";
		String blobname = "";
		
		Path p = Paths.get("index");
		try {
			s = Files.readString(p);
			
			while (s.indexOf(":") != -1) {
				blobname = s.substring(0,s.indexOf(":")-1);
				sub = s.substring(s.indexOf(":")+2, s.indexOf(":")+42);
				s = s.substring(s.indexOf(":")+44);
				list.add("blob : " + sub + " " + blobname);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tree = new Tree (list);
		
		Index.empty();
		
		try {
			Files.writeString(p, "", StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Tree getTree() {
		return tree;
	}
	public void setChild(String child) {
		c = child;
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
		if (p != null) {
			return encrypt(summary + date + name + p.generateSHA1());
		}
		else {
			return encrypt(summary + date + name);
		}
	}
	public String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		date = dtf.format(now);  
		return date;
	}
	public void writeFile() throws FileNotFoundException {
		if (p != null) {
			//Path path = Paths.get(p.generateSHA1());
			
			File remove = new File("objects", p.generateSHA1());
			remove.delete();
			
			p.setChild(generateSHA1());
			
			p.writeFile();
		}
		
		/*
		if (p != null) {
			String s = generateSHA1 ();
			Path path = Paths.get(p);
			String fileStr = "";
			try {
				fileStr = Files.readString(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String temp = fileStr.substring (0,fileStr.indexOf("\n")+2);
			fileStr = fileStr.substring (fileStr.indexOf("\n")+2);
			temp = temp + fileStr.substring (0,fileStr.indexOf("\n")+2);
			fileStr = fileStr.substring (fileStr.indexOf("\n")+2);
			
			
			try {
				Files.writeString(path, temp + s + fileStr, StandardCharsets.ISO_8859_1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		
		File f = new File("objects", generateSHA1());
		PrintWriter pw = new PrintWriter(f);
		pw.println(tree.generateSHA1());
		if (p != null) {
			pw.println(p.generateSHA1());
		}
		else {
			pw.println(p);
		}
		pw.println(c);
		pw.println(name);
		pw.println(date);
		pw.println(summary);
		pw.close();
	}
	
	
}
