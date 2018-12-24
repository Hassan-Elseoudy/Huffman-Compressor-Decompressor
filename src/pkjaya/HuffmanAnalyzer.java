package pkjaya;

public class HuffmanAnalyzer {

	private String fileName;
	private Character choice;

	public HuffmanAnalyzer(String fileName, Character choice) throws Exception {
		this.fileName = fileName;
		this.choice = choice;
		if (choice.equals('c'))
			new Compressor(this.fileName);
		else
			new Decompressor(this.fileName);

	}

}
