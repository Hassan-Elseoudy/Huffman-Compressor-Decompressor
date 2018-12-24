package pkjaya;

import java.io.*;
import java.util.*;

public class Compressor {

	private HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	private String fileName;
	private Node root;
	private File outputFile = new File("compressed.txt");
	private String secret = "HuffmanYaRgola";
	private HashMap<String, String> updatedCodes = new HashMap<String, String>();
	private int fileOrFolder;
	private BufferedWriter out;

	public Compressor(String fileName, int fileOrFolder) throws IOException {
		this.fileName = fileName;
		this.fileOrFolder = fileOrFolder;
		if (fileOrFolder == 0)
			compress(this.fileName);
		else {
			String args[] = readAllFiles(this.fileName);
			for (int i = 0; i < args.length; i++) {
				this.fileName = fileName;
				this.fileName += "//" + args[i];
				compress(this.fileName);
			}
		}
	}

	private String[] readAllFiles(String str) {
		File aDirectory = new File(str);
		String[] args = aDirectory.list();
		return args;
	}

	private void compress(String str) throws IOException {
		this.dictionary = makeDictionary(str);
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
		dictionary.clear();
	}

	private void writeIntoAFile(PriorityQueue<Node> pq, File outputFile) {

		try {
			PriorityQueue<Node> copy = new PriorityQueue<Node>(pq);
			Scanner scanner = new Scanner(new File(this.fileName));
			String text = scanner.useDelimiter("\\A").next();
			scanner.close();
			String zeroOnesString = "";
			out = new BufferedWriter(new FileWriter(outputFile, true));
			int lengthOfNodes = pq.size();
			for (int i = 0; i < lengthOfNodes; i++) {
				Node q = pq.poll();
				if (q.getCharacter().contains(System.lineSeparator())) {
					updatedCodes.put(System.lineSeparator(), q.getCode());
					continue;
				}
				updatedCodes.put(q.getCharacter(), q.getCode());
			}
			for (int i = 0; i < text.length(); i++)
				zeroOnesString += updatedCodes.get(Character.toString(text.charAt(i))); // GetCodes

			/** I need to calculate the bytes size and write it */
			BitSet bitSet = getBitSet(zeroOnesString);
			byte[] writeBytes = bitSet.toByteArray();
			out.write(writeBytes.length + System.getProperty("line.separator")); // OK

			/** Let's write the table now */
			lengthOfNodes = copy.size();
			for (int i = 0; i < lengthOfNodes; i++) {
				Node q = copy.poll();
				out.write(q.getCharacter() + "_:_" + q.getCode() + System.getProperty("line.separator")); // OK
			}

			out.write("<<====>>" + System.getProperty("line.separator")); // End of the header
			out.close();
			/** Let the 0s and 1s begin */
			BinaryOut binOut = new BinaryOut("compressed.txt");
			for (int i = 0; i < zeroOnesString.length(); i++) {
				if (zeroOnesString.charAt(i) == '0')
					binOut.write(false); // Write 0 to the file
				else
					binOut.write(true); // Write 1 to the file
			}
			binOut.close();
			out = new BufferedWriter(new FileWriter(outputFile, true));
			out.write(System.getProperty("line.separator") + "END" + System.getProperty("line.separator"));
			out.close();
			updatedCodes.clear();
			zeroOnesString = "";
		} catch (Exception e) {
			System.err.println("Error while writing to file: " + e.getMessage());
		}
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

	private HashMap<String, Integer> makeDictionary(String str) {
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		try {

			FileInputStream file = new FileInputStream(str);
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

	public Node getRoot() {
		return root;
	}

	public File getOutputFile() {
		return outputFile;
	}

}