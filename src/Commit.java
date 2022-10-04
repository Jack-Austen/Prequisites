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
	String prevTree;
	Tree tree;
	public Commit(String author, String description) {//Also need to get rid of parent?, not needed
		p = getHead();
		//System.out.println (p);
		c="";
		summary = description;
		name = author;
		date = getDate();
		ArrayList<String> list = new ArrayList <String> ();
		String s = "";
		Path fileName;
		if(!(p.equals(""))) {
			fileName = Path.of("C:\\Users\\jacka\\eclipse-workspace\\Prequisites\\objects\\"+p);
			try {
				s = Files.readString(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add("tree : " + s.substring(0,40));//Fix this
			prevTree = s.substring(0,40);
		}
		
		String sub = "";
		String blobname = "";
		
		Path p = Paths.get("index");
		try {
			s = Files.readString(p);
			
			while (s.indexOf(":") != -1) {
				if (s.indexOf("*")>s.indexOf(":")||s.indexOf("*")<0) {
					blobname = s.substring(0,s.indexOf(":")-1);
					sub = s.substring(s.indexOf(":")+2, s.indexOf(":")+42);
					s = s.substring(s.indexOf(":")+44);
					list.add("blob : " + sub + " " + blobname);
				}
				else {
					if(s.charAt(1)=='e') {
						list = deledit (list, s.substring(s.indexOf(":")+2, s.indexOf(":")+42), prevTree, false);
						list.remove("tree : " + prevTree);
						s = s.substring(s.indexOf(":")+44);
					}
					else {
						list = deledit (list, s.substring(s.indexOf(":")+2, s.indexOf(":")+42), prevTree, false);
						list.remove("tree : " + prevTree);
						s = s.substring(s.indexOf(":")+44);
					}
				}
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
		
		Path pathH = Paths.get("HEAD");
		try {
			Files.writeString(pathH, generateSHA1(), StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList <String> deledit (ArrayList <String> list, String hash, String tree, boolean delay){
		boolean flag = false;
		boolean nextTreeHasUpdated = false;
		String str = "";
		String nextTree = "";
		
		Path fileName = Path.of("C:\\Users\\jacka\\eclipse-workspace\\Prequisites\\objects\\"+tree);
		try {
			str = Files.readString(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while (str.indexOf(":") != -1) {
			if (str.charAt(0)=='b'&&!str.substring(7,47).equals(hash)) {
				list.add(str.substring(0,str.indexOf("\n")));
				str = str.substring(str.indexOf("\n")+1);
			}
			else if (str.charAt(0)=='t') {
				nextTree = str.substring(7,47);
				str = str.substring(str.indexOf("\n")+1);
				nextTreeHasUpdated = true;
			}
			else {
				flag = true;
				str = str.substring(str.indexOf("\n")+1);
			}
		}
		if (flag == false && delay == false) {
			return deledit(list,hash,nextTree,false);
		}
		else if (flag == true){
			if (nextTreeHasUpdated == true) {
				list.add("tree : " + nextTree);
			}
			return list;
		}
		else {
			if (nextTreeHasUpdated == true) {
				list.add("tree : " + nextTree);
			}
			return list;
		}
	}
	
	public Tree getTree() {
		return tree;
	}
	public String getHead() {
		Path pathH = Paths.get("HEAD");
		String str = "";
		try {
			str = Files.readString(pathH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
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
			return encrypt(summary + date + name + p);
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
		if (!(p.equals(""))) {
			String str = "";
			Path fileName;
			fileName = Path.of("C:\\Users\\jacka\\eclipse-workspace\\Prequisites\\objects\\"+p);
			try {
				str = Files.readString(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String result = "";
			result = result + str.substring(0,str.indexOf("\n")+1);
			str = str.substring(str.indexOf("\n")+1);
			result = result + str.substring(0,str.indexOf("\n")+1);
			str = str.substring(str.indexOf("\n")+1);
			result = result + generateSHA1 () + str;
			
			
			try {
				Files.writeString(fileName, result, StandardCharsets.ISO_8859_1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		
		File f = new File("objects", generateSHA1());
		PrintWriter pw = new PrintWriter(f);
		pw.println(tree.generateSHA1());
		pw.println(p);
		pw.println(c);
		pw.println(name);
		pw.println(date);
		pw.println(summary);
		pw.close();
	}
	
	
}
