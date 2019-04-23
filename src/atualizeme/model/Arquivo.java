package atualizeme.model;

public class Arquivo {

	private String pathFile;
	private String hashFile;
	
	public Arquivo(String pathFile, String hashFile) {
		this.pathFile = pathFile;
		this.hashFile = hashFile;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	public String getHashFile() {
		return hashFile;
	}

	public void setHashFile(String hashFile) {
		this.hashFile = hashFile;
	}
}
