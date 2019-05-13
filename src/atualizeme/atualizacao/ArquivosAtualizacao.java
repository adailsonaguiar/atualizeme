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

	public static String NOME_ARQUIVO_VERIFICACAO = "MD5.txt";
	private String nomePastaAplicacao;
	private String caminhoAplicacao;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
	}

	public void arquivomd5() throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		File md5 = new File(getCaminhoAplicacao() + NOME_ARQUIVO_VERIFICACAO);
		if (!md5.exists()) {
			List<Arquivo> lista = listaCaminhos(new File(caminhoAplicacao));
			for (int i = 0; i < lista.size(); i++) {
				writeFile(getCaminhoAplicacao(), NOME_ARQUIVO_VERIFICACAO, lista.get(i).getCaminhoLiteral(),
						lista.get(i).getCaminhoPasta(), geraHash(new File(lista.get(i).getCaminhoLiteral())),
						lista.get(i).getNome());
			}
		} else if (md5.delete()) {
			List<Arquivo> lista = listaCaminhos(new File(caminhoAplicacao));
			for (int i = 0; i < lista.size(); i++) {
				writeFile(getCaminhoAplicacao(), NOME_ARQUIVO_VERIFICACAO, lista.get(i).getCaminhoLiteral(),
						lista.get(i).getCaminhoPasta(), geraHash(new File(lista.get(i).getCaminhoLiteral())),
						lista.get(i).getNome());
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
		List<Arquivo> caminhos = new ArrayList<Arquivo>();
		if (dir == null || dir.listFiles() == null) {
			return null;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				String[] dados = null;
				dados = file.getParent().split(nomePastaAplicacao, 2);
				if (dados.length > 1) {
					caminhos.add(
							new Arquivo(dados[1] + File.separator, file.getAbsolutePath(), "", file, file.getName()));
				} else {
					caminhos.add(new Arquivo(File.separator, file.getAbsolutePath(), "", file, file.getName()));
				}
			} else
				caminhos.addAll(listaCaminhos(file));
		}
		return (List<Arquivo>) caminhos;
	}

	public String geraHash(File f) throws NoSuchAlgorithmException, FileNotFoundException {
		String date = "" + f.lastModified();
		return date;
	}

	public String getNomePastaAplicacao() {
		return nomePastaAplicacao;
	}

	public void setNomePastaAplicacao(String nomePastaAplicacao) {
		this.nomePastaAplicacao = nomePastaAplicacao;
	}

	public String getCaminhoAplicacao() {
		return caminhoAplicacao;
	}

	public void setCaminhoAplicacao(String caminhoAplicacao) {
		this.caminhoAplicacao = caminhoAplicacao;
	}
}