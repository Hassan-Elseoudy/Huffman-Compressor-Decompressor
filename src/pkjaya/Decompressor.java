package pkjaya;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Decompressor {

	private HashMap<String, String> dictionary = new HashMap<String, String>();
	private String fileName;
	private String outputFile = new String("decompressed.txt");

	public Decompressor(String fileName) throws Exception {
		this.fileName = fileName;
		decompress();
	}

	private void decompress() throws Exception {
		int bytesLength = getBytesLength(this.fileName); // Get Coded Length
		dictionary = readDictionaryTable(this.fileName);
		byte[] allFile = Files.readAllBytes(Paths.get(this.fileName));
		int fileSize = allFile.length; // Get Whole size
		String zerosAndOnes = getEncodedString(allFile, fileSize, bytesLength);
		String LetsGetItBack = getActualString(zerosAndOnes, dictionary);
		System.out.println(LetsGetItBack);
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFile));
		writer.write(LetsGetItBack + "\n");
		writer.close();

	}

	String getActualString(String zerosAndOnes, HashMap<String, String> dictionary) {
		String s = "";
		String decoded = "";
		for (int i = 0; i < zerosAndOnes.length(); i++) {
			s += zerosAndOnes.charAt(i);
			if (dictionary.containsKey(s)) {
				decoded += dictionary.get(s);
				s = "";
			}
		}
		return decoded;
	}

	int getBytesLength(String str) throws Exception {
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new FileReader(str));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Integer.parseInt(buff.readLine()));
	}

	HashMap<String, String> readDictionaryTable(String str) throws IOException {
		HashMap<String, String> hm = new HashMap<String, String>();
		String temp;
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new FileReader(str));
			temp = buff.readLine(); // I Don't want the first line
			while (!(temp = buff.readLine()).contains("<<====>>")) {
				String[] arr = temp.split("_:_");
				if (arr.length == 2) {
					if (arr[0].length() == 0) {
						hm.put(arr[1], System.getProperty("line.separator"));
						continue;
					}
					hm.put(arr[1], arr[0]);
				} else
					continue;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hm;
	}

	String getEncodedString(byte[] allFile, int fileSize, int bytesLength) {
		String s = "";
		/**
		 * Dealing with file bytes.
		 * 5
		 * a = 
		 * b = 
		 * c = 
		 * <====>
		 * bytesLength
		 */
		for (int i = (fileSize - bytesLength); i < fileSize - 1; i++) {
			int temp = (byte) allFile[i] & 0xFF;
			s += String.format("%8s", Integer.toBinaryString(temp)).replace(' ', '0');
		}
		/**
		 * Dealing with the last byte.
		 */
		char lastShit = (char) allFile[allFile.length - 1];
		String lastShitaSString = String.format("%8s", Integer.toBinaryString(lastShit)).replace(' ', '0');
		int idx = -1;
		for (int i = 0; i < lastShitaSString.length(); i++) {
			if (lastShitaSString.charAt(i) == '1')
				idx = i;
		}
		s += lastShitaSString.substring(0, idx + 1);
		return s;
	}
}
