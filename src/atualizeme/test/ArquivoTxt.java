package atualizeme.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import atualizeme.model.Arquivo;

class ArquivoTxt {

	private static String caminho = System.getProperty("user.home") + File.separator + "oias" + File.separator;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

//		List<?> lista = listFileTree(new File(caminho));
//		for (int i = 0; i < lista.size(); i++) {
//			writeFile(caminho, "teste.txt", lista.get(i).toString(), geraHash(new File(lista.get(i).toString())));
//		}
		List<Arquivo> lista = readFile(caminho + "teste2.txt");
		List<Arquivo> lista2 = readFile(caminho + "teste.txt");

		comparaListas(lista, lista2);

	}

	public static void criaArquivo(String caminho, String nome) {
		try {
			FileWriter arquivo = new FileWriter(new File(caminho + nome));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(String caminho, String nome, String caminhodir, String hashDir) throws IOException {
		FileWriter write = new FileWriter(caminho + nome, true);
		write.write(caminhodir);
		write.write(";");
		write.write(hashDir);
		write.write("\n");
		write.flush();
		write.close();
	}

	public static List<Arquivo> readFile(String pathFile) throws IOException {
		List<Arquivo> content = new ArrayList<>();
		FileReader arq = new FileReader(pathFile);
		BufferedReader buffer = new BufferedReader(arq);
		String linha = null;

		while ((linha = buffer.readLine()) != null) {

			String[] dados = linha.split(";");
			content.add(new Arquivo(dados[0].toString(), dados[1].toString()));
		}
		return content;
	}

	public static List<File> listFileTree(File dir) {
		List<File> fileTree = new ArrayList<File>();
		if (dir == null || dir.listFiles() == null) {
			List<File> fileTree2 = (List<File>) fileTree;
			return fileTree2;
		}
		for (File entry : dir.listFiles()) {
			if (entry.isFile())
				fileTree.add(entry);
			else
				fileTree.addAll(listFileTree(entry));
		}
		return (List<File>) fileTree;
	}

	public static String geraHash(File f) throws NoSuchAlgorithmException, FileNotFoundException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		InputStream is = new FileInputStream(f);
		byte[] buffer = new byte[8192];
		int read = 0;
		String output = null;
		try {
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			output = bigInt.toString(16);
		} catch (IOException e) {
			throw new RuntimeException("Não foi possivel processar o arquivo.", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException("Não foi possivel fechar o arquivo", e);
			}
		}
		return output;
	}

	public static void comparaListas(List<Arquivo> lista, List<Arquivo> lista2) {
		for (int i = 0; i < lista.size(); i++) {
			for (int j = 0; j < lista2.size(); j++) {
				if (lista.get(i).getPathFile().equals(lista2.get(j).getPathFile())) {
					if (!lista.get(i).getHashFile().equals(lista2.get(j).getHashFile())) {
						System.out.println("Atualizando arquivo!");
						// Chama m�todo que atualiza arquivo ..
						// ..passando o caminho do arquivo "lista.get(i).getPathFile()"

						// Atualiza arquivo TXT do cliente ..
					}
				}
			}
		}
	}
}
