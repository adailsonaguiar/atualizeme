package atualizeme.atualizacao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import atualizeme.model.Arquivo;

public class ArquivosAtualizacao {

	private static String NOME_PASTA_APLICACAO = "oias";
	private static String nome;
	private String caminhoAplicacao;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		ArquivosAtualizacao.nome = nome;
	}

	public String getPastaAplicacao() {
		return caminhoAplicacao;
	}

	public void setPastaAplicacao(String pastaAplicacao) {
		this.caminhoAplicacao = pastaAplicacao;
	}

	public void arquivomd5(String caminhoMD5, String nomeMD5)
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		File md5 = new File(caminhoMD5 + nomeMD5);
		if (!md5.exists()) {
			List<Arquivo> lista = listaCaminhos(new File(caminhoAplicacao));
			for (int i = 0; i < lista.size(); i++) {
				writeFile(caminhoMD5, nomeMD5, lista.get(i).getCaminhoLiteral(), lista.get(i).getCaminhoPasta(),
						geraHash(new File(lista.get(i).getCaminhoLiteral())), lista.get(i).getNome());
			}
		} else if (md5.delete()) {
			List<Arquivo> lista = listaCaminhos(new File(caminhoAplicacao));
			for (int i = 0; i < lista.size(); i++) {
				writeFile(caminhoMD5, nomeMD5, lista.get(i).getCaminhoLiteral(), lista.get(i).getCaminhoPasta(),
						geraHash(new File(lista.get(i).getCaminhoLiteral())), lista.get(i).getNome());
			}

		}
	}

	public void writeFile(String caminhoMD5, String nomeMD5, String caminhoLiteral, String caminhoPasta, String hashDir,
			String nomeArquivo) throws IOException {
		FileWriter write = new FileWriter(caminhoMD5 + nomeMD5, true);
		write.write(caminhoLiteral);
		write.write(";");
		write.write(caminhoPasta);
		write.write(";");
		write.write(hashDir);
		write.write(";");
		write.write(nomeArquivo);
		write.write("\n");
		write.flush();
		write.close();
	}

	public List<Arquivo> readFile(String pathFile) throws IOException {

		List<Arquivo> content = new ArrayList<Arquivo>();
		FileReader arq = new FileReader(pathFile);
		BufferedReader buffer = new BufferedReader(arq);
		String linha = null;

		while ((linha = buffer.readLine()) != null) {

			String[] dados = linha.split(";");
			// String caminhoPasta, String caminhoLiteral, String hashFile, File file
			content.add(new Arquivo(dados[1].toString(), dados[0].toString(), dados[2].toString(), null,
					dados[3].toString()));
		}
		buffer.close();
		return content;
	}

	public List<Arquivo> listaCaminhos(File dir) {
		// File dir ---> Pasta onde se quer listar os caminhos
		List<Arquivo> fileTree = new ArrayList<Arquivo>();
		if (dir == null || dir.listFiles() == null) {
			List<Arquivo> fileTree2 = (List<Arquivo>) fileTree;
			return fileTree2;
		}
		for (File entry : dir.listFiles()) {
			if (entry.isFile()) {
				String[] dados = null;
				String os = System.getProperty("os.name");
				if (os.contains("Windows")) {
					dados = entry.getAbsolutePath().split(NOME_PASTA_APLICACAO + File.separator + File.separator);
				} else {
					dados = entry.getAbsolutePath().split(File.separator + NOME_PASTA_APLICACAO);
				}
				fileTree.add(new Arquivo(dados[1], entry.getAbsolutePath(), "", entry, entry.getName()));
			} else
				fileTree.addAll(listaCaminhos(entry));
		}
		return (List<Arquivo>) fileTree;
	}

	public String geraHash(File f) throws NoSuchAlgorithmException, FileNotFoundException {
		String date = "" + f.lastModified();
		return date;
	}

	public List<Arquivo> excluirArquivos(List<Arquivo> listaServidor, List<Arquivo> listacliente) {
		List<Arquivo> arqExlusao = new ArrayList<Arquivo>();
		for (int i = 0; i < listacliente.size(); i++) {
			if (!listaServidor.contains(listacliente.get(i))) {
				arqExlusao.add(new Arquivo(listacliente.get(i).getCaminhoPasta(), "", "", null, ""));
			}
		}
		return arqExlusao;
	}

	public List<Arquivo> adiconarArquivos(List<Arquivo> listaServidor, List<Arquivo> listacliente) {
		List<Arquivo> arqAdicionar = new ArrayList<Arquivo>();
		for (int i = 0; i < listacliente.size(); i++) {
			if (!listacliente.contains(listaServidor.get(i))) {
				arqAdicionar.add(new Arquivo(listaServidor.get(i).getCaminhoPasta(), "", "", null, ""));
			}
		}
		return arqAdicionar;
	}

	public List<Arquivo> comparaListas(List<Arquivo> listaServidor, List<Arquivo> listacliente) {
		List<Arquivo> arqEnvio = new ArrayList<Arquivo>();
		for (int i = 0; i < listacliente.size(); i++) {
			if (listacliente.contains(listaServidor.get(i))) {
				for (int j = 0; j < listacliente.size(); j++) {
					if (listacliente.get(j).getCaminhoPasta().equals(listaServidor.get(i).getCaminhoPasta())) {
						if (!listacliente.get(j).getHashFile().equals(listaServidor.get(i).getHashFile())) {
							arqEnvio.add(listacliente.get(j));
						}
					}
				}
			}
		}
		return arqEnvio;
	}

}
