import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Blob {
	private static File end;
	private String filesName;
	public Blob(String origFile) throws IOException, NoSuchAlgorithmException {
		File oldFile = new File(origFile);
		byte[] input = convertToByte(oldFile.getAbsoluteFile());
		filesName = SHAsum(input);
		File newFile = new File("objects", filesName);
		copyFileUsingStream(oldFile, newFile);
	}
	 public static byte[] convertToByte(File file) throws IOException
		    {
		        // Creating an object of FileInputStream to
		        // read from a file
		        FileInputStream fl = new FileInputStream(file);
		  
		        // Now creating byte array of same length as file
		        byte[] arr = new byte[(int)file.length()];
		  
		        // Reading file content to byte array
		        // using standard read() method
		        fl.read(arr);
		  
		        // lastly closing an instance of file input stream
		        // to avoid memory leakage
		        fl.close();
		  
		        // Returning above byte array
		        return arr;
		    }
	public static String SHAsum(byte[] convertme) throws NoSuchAlgorithmException{
	    MessageDigest md = MessageDigest.getInstance("SHA-1"); 
	    return byteArray2Hex(md.digest(convertme));
	}

	private static String byteArray2Hex(final byte[] hash) {
	    Formatter formatter = new Formatter();
	    for (byte b : hash) {
	        formatter.format("%02x", b);
	    }
	    return formatter.toString();
	}
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	    end = dest;
	}
	public String getSHA1() {
		return filesName;
	}
	public File getFile() {
		return end;
	}
}