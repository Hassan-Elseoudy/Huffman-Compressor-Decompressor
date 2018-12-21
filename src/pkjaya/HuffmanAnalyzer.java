package pkjaya;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HuffmanAnalyzer {

	private HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	private String fileName;
	private Character choice;
	private Node root;
	private File outputFile = new File("output.txt");
	private String secret = "HuffmanYaRgola";
	private HashMap<String, String> updatedCodes = new HashMap<String, String>();
	private FileOutputStream fos;

	public HuffmanAnalyzer(String fileName, Character choice) throws IOException {
		this.fileName = fileName;
		this.choice = choice;
		if (choice.equals('c'))
			compress();
		else
			decompress();

	}

	private void compress() throws IOException {
		this.dictionary = makeDictionary();
		PriorityQueue<Node> pq = new PriorityQueue<Node>(dictionary.size(), new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return o1.getFrequency() - o2.getFrequency();
			}
		});

		for (int i = 0; i < dictionary.size(); i++) {
			pq.add(new Node((String) dictionary.keySet().toArray()[i], dictionary.get(dictionary.keySet().toArray()[i]),
					null, null, null, ""));
		}

		PriorityQueue<Node> listOfNodes = new PriorityQueue<Node>(pq);
		// Huff-man tree
		while (pq.size() != 1) {
			Node o1 = pq.remove();
			Node o2 = pq.remove();
			Node parent = new Node(this.secret, o1.getFrequency() + o2.getFrequency(), null, null, null, "");
			o1.setParent(parent);
			o2.setParent(parent);
			parent.setLeft(o1);
			parent.setRight(o2);
			pq.add(parent);
		}
		this.root = pq.remove();
		giveCodes(root, "0");
		writeIntoAFile(listOfNodes, this.outputFile);

	}

	private void decompress() {

	}

	private BitSet getBitSet(String str) {
		BitSet bitSet = new BitSet(str.length());
		int bitcounter = 0;
		for (Character c : str.toCharArray()) {
			if (c.equals('1')) {
				bitSet.set(bitcounter);
			}
			bitcounter++;
		}
		return bitSet;
	}

	private void writeIntoAFile(PriorityQueue<Node> pq, File outputFile) {

		try {
			PriorityQueue<Node> copy = new PriorityQueue<Node>(pq);
			Scanner scanner = new Scanner(new File(this.fileName));
			String text = scanner.useDelimiter("\\A").next();
			scanner.close();
			String zeroOnesString = "";
			
			FileWriter fstream = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fstream);
			
			int lengthOfNodes = pq.size();
			for (int i = 0; i < lengthOfNodes; i++) {
				Node q = pq.poll();
				updatedCodes.put(q.getCharacter(), q.getCode());
			}
			
			lengthOfNodes = copy.size();
			for (int i = 0; i < lengthOfNodes; i++) {
				Node q = copy.poll();
				out.write(q.getCharacter() + ":" + q.getCode() + "\n");
			}
			out.write("<<====>>\n");
			for (int i = 0; i < text.length(); i++)
				zeroOnesString += updatedCodes.get(Character.toString(text.charAt(i)));
			BitSet bitSet = getBitSet(zeroOnesString);
			byte[] writeBytes = bitSet.toByteArray();
			out.close();
			fos = new FileOutputStream("output.txt",true);
			fos.write(writeBytes);
			fos.close();
		} catch (Exception e) {
			System.err.println("Error while writing to file: " + e.getMessage());
		}
	}

	private HashMap<String, Integer> makeDictionary() {
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		try {

			FileInputStream file = new FileInputStream(this.fileName);
			String read;
			while (file.available() > 1) {
				Character temp = (char) file.read();
				read = temp.toString();
				if (!dict.containsKey(read))
					dict.put(read, 0);
				dict.put(read, dict.get(read) + 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dict;
	}

	private void giveCodes(Node node, String str) {

		if (node == null)
			return;
		giveCodes(node.getLeft(), (str.substring(0, str.length()) + "0"));
		giveCodes(node.getRight(), (str.substring(0, str.length()) + "1"));
		node.setCode(str);

	}

	public HashMap<String, Integer> getDictionary() {
		return dictionary;
	}

	public String getFileName() {
		return fileName;
	}

	public Character getChoice() {
		return choice;
	}

	public Node getRoot() {
		return root;
	}

	public File getOutputFile() {
		return outputFile;
	}

}
