package pkjaya;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Decompressor {
	
	private HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	private String fileName;
	private Character choice;
	private Node root;
	private String outputFile = new String("decompressed.txt");
	private String secret = "HuffmanYaRgola";
	private HashMap<String, String> updatedCodes = new HashMap<String, String>();
	private FileOutputStream fos;
	
	public Decompressor(String fileName) throws IOException {
		this.fileName = fileName;
		decompress();
	}

	private void decompress() throws IOException {

		String contents = new String(Files.readAllBytes(Paths.get(this.fileName))).
				substring(new String(Files.readAllBytes(Paths.get(this.fileName))).
						indexOf("<<====>>") + 9);
		contents = contents.substring(0, contents.length() - 1);
		System.out.println(contents);
	}
	
	// stringToBinary
	public static String stringToBinary(String message) {
	    StringBuilder binary = new StringBuilder();
	    for (char c : message.toCharArray()) {
	        binary.append(Integer.toBinaryString(c));
	    }
	    return binary.toString();
	}
	
	private String getZeroOneString() {
		
		return null;
	}

}
