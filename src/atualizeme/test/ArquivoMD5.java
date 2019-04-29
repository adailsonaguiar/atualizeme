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

import atualizeme.model.ArquivoTxt;

public class ArquivoMD5 {

	private static String nome;
	private String pastaAplicacao;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
//		ArquivoTxt no = new ArquivoTxt("Nova pasta\\Nova pasta (2)\\3a2we1fa3efdad.txt",
//				"C:\\Users\\adailsonacj\\oias\\Nova pasta\\Nova pasta (2)\\3a2we1fa3efdad.txt",
//				"babe6d557f9ed15b23db20a025fa148", null);
//		

		List<ArquivoTxt> lista = new ArrayList<ArquivoTxt>();
		lista.add(new ArquivoTxt("Nova pasta\\Nova pasta (2)\\3a2we1fa3efdad.txt",
				"C:\\Users\\adailsonacj\\oias\\Nova pasta\\Nova pasta (2)\\3a2we1fa3efdad.txt",
				"babe6d557f9ed15b23db20a025fa148", null));

		List<ArquivoTxt> lista2 = new ArrayList<ArquivoTxt>();
		lista2.add(new ArquivoTxt("Nova psasta\\Nova pasta (2)\\3a2we1fa3efdad.txt",
				"C:\\Users\\adailsonacj\\oias\\Nova pasta\\Nova pasta (2)\\3a2we1fa3efdad.txt",
				"babe6d557f9ed15b23db20a025fa148", null));

		if (lista.contains(lista2.get(0))) {
			System.out.println(true);
		}
		System.out.println(false);

//		List<ArquivoTxt> lista = readFile(
//				System.getProperty("user.home") + File.separator + "aaa" + File.separator + "MD5.txt");
//		List<ArquivoTxt> lista2 = readFile(
//				System.getProperty("user.home") + File.separator + "aaa" + File.separator + "2MD5.txt");
//		arquivosExcluir(lista, lista2);
		// C:\\Users\\adailsonacj\\oias\\Nova pasta\\Nova pasta (2)\\3a2we1fa3efdad.txt
		// C:\\Users\\adailsonacj\\oias\\Nova pasta\\Nova pasta (2)\\3a2we1fa3efdad.txt

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		ArquivoMD5.nome = nome;
	}

	public String getPastaAplicacao() {
		return pastaAplicacao;
	}

	public void setPastaAplicacao(String pastaAplicacao) {
		this.pastaAplicacao = pastaAplicacao;
	}

	public void arquivomd5(String caminhoMD5, String nomeMD5)
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		File md5 = new File(caminhoMD5 + nomeMD5);
		if (!md5.exists()) {
			List<ArquivoTxt> lista = listaCaminhos(new File(pastaAplicacao));
			for (int i = 0; i < lista.size(); i++) {
				writeFile(caminhoMD5, nomeMD5, lista.get(i).getCaminhoLiteral(), lista.get(i).getCaminhoPasta(),
						geraHash(new File(lista.get(i).getCaminhoLiteral())));
			}
		} else if (md5.delete()) {
			List<ArquivoTxt> lista = listaCaminhos(new File(pastaAplicacao));
			for (int i = 0; i < lista.size(); i++) {
				writeFile(caminhoMD5, nomeMD5, lista.get(i).getCaminhoLiteral(), lista.get(i).getCaminhoPasta(),
						geraHash(new File(lista.get(i).getCaminhoLiteral())));
			}

		}
	}

	public void writeFile(String caminhoMD5, String nomeMD5, String caminhoLiteral, String caminhoPasta, String hashDir)
			throws IOException {
		FileWriter write = new FileWriter(caminhoMD5 + nomeMD5, true);
		write.write(caminhoLiteral);
		write.write(";");
		write.write(caminhoPasta);
		write.write(";");
		write.write(hashDir);
		write.write("\n");
		write.flush();
		write.close();
	}

	public static List<ArquivoTxt> readFile(String pathFile) {

		List<ArquivoTxt> content = new ArrayList<>();
		try {
			FileReader arq = new FileReader(pathFile);
			BufferedReader buffer = new BufferedReader(arq);
			String linha = null;

			while ((linha = buffer.readLine()) != null) {

				String[] dados = linha.split(";");
				// String caminhoPasta, String caminhoLiteral, String hashFile, File file
				content.add(new ArquivoTxt(dados[1].toString(), dados[0].toString(), dados[2].toString(), null));
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public List<ArquivoTxt> listaCaminhos(File dir) {
		// File dir ---> Pasta onde se quer listar os caminhos
		List<ArquivoTxt> fileTree = new ArrayList<ArquivoTxt>();
		if (dir == null || dir.listFiles() == null) {
			List<ArquivoTxt> fileTree2 = (List<ArquivoTxt>) fileTree;
			return fileTree2;
		}
		for (File entry : dir.listFiles()) {
			if (entry.isFile()) {
//				fileTree.add(entry);
				String[] dados = entry.getAbsolutePath().split("oias" + File.separator + File.separator);
				fileTree.add(new ArquivoTxt(dados[1], entry.getAbsolutePath(), "", entry));
			} else
				fileTree.addAll(listaCaminhos(entry));
		}
		return (List<ArquivoTxt>) fileTree;
	}

	public String geraHash(File f) throws NoSuchAlgorithmException, FileNotFoundException {
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

	public List<ArquivoTxt> comparaArquivosMD5(List<ArquivoTxt> listaServidor, List<ArquivoTxt> listacliente) {
		List<ArquivoTxt> arqEnvio = new ArrayList<>();
		for (int i = 0; i < listaServidor.size(); i++) {
			for (int j = 0; j < listacliente.size(); j++) {
				if (listaServidor.get(i).getCaminhoPasta().equals(listacliente.get(j).getCaminhoPasta())) {
					if (!listaServidor.get(i).getHashFile().equals(listacliente.get(j).getHashFile())) {
						arqEnvio.add(new ArquivoTxt(listaServidor.get(i).getCaminhoPasta(),
								listaServidor.get(i).getCaminhoLiteral(), listaServidor.get(i).getHashFile(), null));
					}
				}
			}
		}
		return arqEnvio;
	}

	public static List<ArquivoTxt> arquivosExcluir(List<ArquivoTxt> listaServidor, List<ArquivoTxt> listacliente) {
		List<ArquivoTxt> arqExlusao = new ArrayList<>();
		for (int i = 0; i < listacliente.size(); i++) {
//		for (int j = 0; j < listaServidor.size(); j++) {
//			if (listacliente.get(i).equals(listaServidor.get(j).getCaminhoPasta())) {
//				arqExlusao.add(new ArquivoTxt(listaServidor.get(i).getCaminhoPasta(),
//						listaServidor.get(i).getCaminhoLiteral(), listaServidor.get(i).getHashFile(), null));
//				System.out.println(listaServidor.get(i).getCaminhoLiteral());
//			}
//		}
		}
		return arqExlusao;
	}
}
