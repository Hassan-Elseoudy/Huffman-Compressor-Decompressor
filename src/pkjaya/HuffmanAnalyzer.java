package pkjaya;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.lang.Object;

public class HuffmanAnalyzer {

	private HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	private String fileName;
	private Character choice;
	private Node root;
	private File outputFile = new File("output.txt");
	private String secret = "HuffmanYaRgola";
	private HashMap<String, String> updatedCodes = new HashMap<String, String>();

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
		int lengthOfNodes = listOfNodes.size();
		for (int i = 0; i < lengthOfNodes; i++) {
			Node q = listOfNodes.poll();
			updatedCodes.put(q.getCharacter(), q.getCode());
			System.out.println("Character is: " + q.getCharacter() + "Code is: " + q.getCode() + "Frequency is:"
					+ q.getFrequency());
		}

		Scanner scanner = new Scanner(new File(this.fileName));
		String text = scanner.useDelimiter("\\A").next();
		text = text.replaceAll("\n|\r", "");
		String zerosAndOnes = "";
		scanner.close(); // Put this call in a finally block
		for (int i = 0; i < text.length(); i++)
			zerosAndOnes += updatedCodes.get(Character.toString(text.charAt(i)));

		System.out.println(zerosAndOnes);

		BitSet bitSet = getBitSet(zerosAndOnes);
		byte[] writeBytes = bitSet.toByteArray();
		try (FileOutputStream fos = new FileOutputStream("output.txt")) {
			fos.write(writeBytes);
		}

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

	private HashMap<String, Integer> makeDictionary() throws FileNotFoundException {
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		FileInputStream file = new FileInputStream(this.fileName);
		String read;
		try {
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

	int count = 0;

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
