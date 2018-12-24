package pkjaya;

import java.util.Scanner;

public class Main {

	private static Scanner s;
	private static HuffmanAnalyzer huff;

	public static void main(String[] args) throws Exception {
		s = new Scanner(System.in);
		System.out.print("Please, enter the file name: ");
		String fileName = s.nextLine();
		System.out.print("Please enter type of usage 'c' for compression, 'd' for decompression: ");
		Character type = s.nextLine().charAt(0);
		long start = System.currentTimeMillis();
		huff = new HuffmanAnalyzer(fileName, type);
		long end = System.currentTimeMillis();
		System.out.println("File is compressed in: " + (end - start) + " ms");
	}

}
