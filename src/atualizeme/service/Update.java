package atualizeme.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.core.Response.ResponseBuilder;

import atualizeme.model.Arquivo;
import atualizeme.test.*;

@Path("update")
public class Update {

	private static String caminho = System.getProperty("user.home") + File.separator + "oias" + File.separator;

	public static void main(String[] args) {

		List<Arquivo> lista = ArquivoTxt.readFile(caminho + "teste2.txt");
		List<Arquivo> lista2 = ArquivoTxt.readFile(caminho + "teste.txt");

		List<Arquivo> lista3 = ArquivoTxt.comparaListas(lista, lista2);

		for (int i = 0; i < lista3.size(); i++) {
			System.out.println(lista3.get(i).getPathFile());
		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String teste() {
		return "teste realizado com sucesso.";
	}

	@javax.ws.rs.GET
	@Path("/get")
	@javax.ws.rs.Produces({ "application/json" })
	public Response getArquivos() throws NoSuchAlgorithmException, FileNotFoundException {

		List<Arquivo> lista = ArquivoTxt.readFile(caminho + "teste2.txt");
		List<Arquivo> lista2 = ArquivoTxt.readFile(caminho + "teste.txt");

		List<Arquivo> lista3 = ArquivoTxt.comparaListas(lista, lista2);

		for (int i = 0; i < lista3.size(); i++) {
//			System.out.println(lista3.get(i).getPathFile());
			File file = new File(lista3.get(i).getPathFile());
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=\"file_from_server.txt\"");
			return response.build();
		}
		return Response.status(Response.Status.OK).type("application/json")
				.entity("Seu hash difere do encontrado no servidor!").build();
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
			System.out.println("MD5: " + output);
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
}
