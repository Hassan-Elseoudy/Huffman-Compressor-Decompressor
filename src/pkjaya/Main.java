package pkjaya;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner s = new Scanner(System.in);
		System.out.print("Please, enter the file name: ");
		String fileName = s.nextLine();
		System.out.print("Please enter type of usage 'c' for compression, 'd' for decompression: ");
		Character type = s.nextLine().charAt(0);
		HuffmanAnalyzer huff = new HuffmanAnalyzer(fileName,type);
		
		System.out.println("dictionary is: " + huff.getDictionary());
	}

}
