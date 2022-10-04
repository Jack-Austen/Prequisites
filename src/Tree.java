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
		StringBuilder all = new StringBuilder();
		for(int i = 0; i < list.size() - 1; i++) {
			all.append(list.get(i));
			all.append("\n");
		}
		all.append(list.get(list.size() - 1));
		System.out.println(all.toString());
		return encrypt(all.toString());
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
