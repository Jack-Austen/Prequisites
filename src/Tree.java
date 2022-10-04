import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
public class Tree {
	private ArrayList<String> list;
	public Tree(ArrayList<String> KVList) {
		list = KVList;
		File f = new File("objects", generateSHA1());
		try {
			PrintWriter pw = new PrintWriter(f);
			for(int i = 0; i < list.size(); i++) {
				pw.println(list.get(i));
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		String all = "";
		for (int i = 0; i < list.size()-1;i++) {
			all = all + list.get(i) + "\n";
		}
		all = all + list.get(list.size()-1);
		return encrypt (all);
	}
	public void createIndexFile() throws FileNotFoundException {
		File indexFile = new File("objects/" + generateSHA1());
		PrintWriter pw = new PrintWriter(indexFile);
		for(String s : list) {
			pw.println(s);
		}
		pw.close();
	}
	//why is there a 0 missing at the front of my file name?

}
