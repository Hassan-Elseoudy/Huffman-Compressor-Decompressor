package pkjaya;

public class HuffmanAnalyzer {

	private String fileName;
	private Character choice;

	public HuffmanAnalyzer(String fileName, Character choice, int fileOrFolder) throws Exception {
		this.fileName = fileName;
		this.choice = choice;
		if (choice.equals('c')) {
			new Compressor(this.fileName,fileOrFolder);
		}
		else
			new Decompressor(this.fileName);

	}

}
