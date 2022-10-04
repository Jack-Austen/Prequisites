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
	String prevTree; //used for delete and edit
	Tree tree;
	public Commit(String author, String description) {//makes a commit
		p = getHead();
		c="";
		summary = description;
		name = author;
		date = getDate();
		ArrayList<String> list = new ArrayList <String> ();
		String s = "";
		//Above is just variable stuff
		
		Path fileName;
		if(!(p.equals(""))) {
			fileName = Path.of("C:\\Users\\jacka\\eclipse-workspace\\Prequisites\\objects\\"+p);//gets the file to read
			try {
				s = Files.readString(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add("tree : " + s.substring(0,40));
			prevTree = s.substring(0,40);//stores the tree hash so we can get it for deletion
		}
		
		String sub = "";
		String blobname = "";
		
		Path p = Paths.get("index");
		try {
			s = Files.readString(p);
			
			while (s.indexOf(":") != -1) {//goes line by line through the list and sorts
				if (s.indexOf("*")>s.indexOf(":")||s.indexOf("*")<0) {
					blobname = s.substring(0,s.indexOf(":")-1);
					sub = s.substring(s.indexOf(":")+2, s.indexOf(":")+42);
					s = s.substring(s.indexOf(":")+44);
					list.add("blob : " + sub + " " + blobname);
				}
				else {//if it is a delete or an edit, it calls the recursive method deledit and then keeps going
					if(s.charAt(1)=='e') {
						list = deledit (list, s.substring(s.indexOf(":")+2, s.indexOf(":")+42), prevTree, false);
						blobname = s.substring(8,s.indexOf(":")-1);
						list.remove("tree : " + prevTree);
						s = s.substring(s.indexOf(":")+44);
						list.add("blob : " + s.substring(s.indexOf(":")+2, s.indexOf(":")+42) + blobname);
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
		
		Index.empty();//empties the index
		
		try {
			Files.writeString(p, "", StandardCharsets.ISO_8859_1);//empties the index
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Path pathH = Paths.get("HEAD");//sets the head
		try {
			Files.writeString(pathH, generateSHA1(), StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList <String> deledit (ArrayList <String> list, String hash, String tree, boolean delay){//this method it scuffed, but it works
		boolean flag = false;
		boolean nextTreeHasUpdated = false;
		String str = "";
		String nextTree = "";
		
		Path fileName = Path.of("C:\\Users\\jacka\\eclipse-workspace\\Prequisites\\objects\\"+tree);//gets the file
		try {
			str = Files.readString(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while (str.indexOf(":") != -1) {//goes line by line and decides outcome based on characteristics
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
		if (flag == false && delay == false) {//this is the recursive case
			return deledit(list,hash,nextTree,false);
		}
		else {//this is the base case, when we find the hash we are looking for and connect to the tree behind it
			if (nextTreeHasUpdated == true) {
				list.add("tree : " + nextTree);
			}
			return list;
		}
	}
	
	public Tree getTree() {
		return tree;
	}
	public String getHead() {//gets the head hash
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
		if (!(p.equals(""))) {//writes the child name in to the parent
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
		
		//below is what actually makes the commit
		
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
