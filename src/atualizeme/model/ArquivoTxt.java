package atualizeme.model;

import java.io.File;
import java.io.IOException;

public class ArquivoTxt {

	private String caminhoPasta;
	private String caminhoLiteral;
	private String hashFile;
	private File file;

	public ArquivoTxt(String caminhoPasta, String caminhoLiteral, String hashFile, File file) {
		this.caminhoPasta = caminhoPasta;
		this.caminhoLiteral = caminhoLiteral;
		this.hashFile = hashFile;
		this.file = file;
	}

	public String getCaminhoPasta() {
		return caminhoPasta;
	}

	public void setCaminhoPasta(String caminhoPasta) {
		this.caminhoPasta = caminhoPasta;
	}

	public String getCaminhoLiteral() {
		return caminhoLiteral;
	}

	public void setCaminhoLiteral(String caminhoLiteral) {
		this.caminhoLiteral = caminhoLiteral;
	}

	public String getHashFile() {
		return hashFile;
	}

	public void setHashFile(String hashFile) {
		this.hashFile = hashFile;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public static void main(String...args) throws IOException {
		File f = new File("C:\\Users\\adailsonacj\\oias\\Nova pasta\\Nova pasta (2)\\sicap.rar");
		System.out.println(f.getCanonicalPath());
		System.out.println(f.getAbsolutePath());
		System.out.println(f.getPath());
		String[] dados = f.getPath().split("oias\\\\");
		System.out.println(dados[1]);
	}
}
