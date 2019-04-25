package atualizeme.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.core.Response.ResponseBuilder;

import atualizeme.model.ArquivoTxt;
import atualizeme.test.*;

import javax.ws.rs.core.Response.Status;

@Path("update")
public class Update {

	private static String caminhoAplicacao = System.getProperty("user.home") + File.separator + "oias" + File.separator;

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, URISyntaxException {

//		List<Arquivo> lista = ArquivoTxt.readFile(caminho + "teste2.txt");
//		List<Arquivo> lista2 = ArquivoTxt.readFile(caminho + "teste.txt");
//
//		List<Arquivo> lista3 = ArquivoTxt.comparaListas(lista, lista2);
//
//		for (int i = 0; i < lista3.size(); i++) {
//			System.out.println(lista3.get(i).getPathFile());
//		}

		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-CLIENTE =-=-=-=-=-=-=-=-=-=-=-=-==--=-=

//		URL url = new URL("http://localhost:8080/atualizeme/api/update/get");
//		File file = new File("C:\\Users\\adailsonacj\\Downloads\\tes.txt");
//
//		InputStream is = url.openStream();
//		FileOutputStream fos = new FileOutputStream(file);
//
//		int bytes = 0;
//
//		while ((bytes = is.read()) != -1) {
//			fos.write(bytes);
//		}
//
//		is.close();
//
//		fos.close();

		/*
		 * try { DefaultHttpClient httpClient = new DefaultHttpClient(); HttpGet
		 * getRequest = new HttpGet("http://localhost:8080/atualizeme/api/update/get");
		 * // getRequest.addHeader("accept", "application/zip"); HttpResponse response =
		 * httpClient.execute(getRequest); if (response.getStatusLine().getStatusCode()
		 * != 200) { throw new RuntimeException("Failed : HTTP error code : " +
		 * response.getStatusLine().getStatusCode()); }
		 * 
		 * /// long l = response.getEntity().getContent()getContentLength(); String
		 * filePath = response.getLastHeader("filePath").getValue();
		 * System.out.println(filePath);
		 * 
		 * BufferedReader br = new BufferedReader(new
		 * InputStreamReader((response.getEntity().getContent())));
		 * 
		 * String output; System.out.println("Output from Server .... \n"); while
		 * ((output = br.readLine()) != null) { //System.out.println(output);
		 * 
		 * }
		 * 
		 * httpClient.getConnectionManager().shutdown();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 * 
		 */

		Client client = ClientBuilder.newClient();
		String url = "http://localhost:8080/atualizeme/api/update/get";
		Response response = client.target(url).request().get();
		String location = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "oias"
				+ File.separator + response.getHeaderString("nomeArquivo");
		FileOutputStream out = new FileOutputStream(location);
		InputStream is = (InputStream) response.getEntity();
		int len = 0;
		byte[] buffer = new byte[4096];
		while ((len = is.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		is.close();

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

		ArquivoMD5 md5 = new ArquivoMD5();
		md5.setNome("MD5.txt");
		md5.setPastaAplicação(caminhoAplicacao);

		List<ArquivoTxt> lista = md5.readFile(md5.getPastaAplicação() + md5.getNome());
		List<ArquivoTxt> listacliente = md5.readFile(md5.getPastaAplicação() + "2" + md5.getNome());

		List<ArquivoTxt> lista3 = md5.comparaArquivosMD5(lista, listacliente);

		for (int i = 0; i < lista3.size(); i++) {
			System.out.println(System.getProperty("user.home") + File.separator + "oias" + File.separator
					+ lista3.get(i).getCaminhoPasta());
			File file = new File(System.getProperty("user.home") + File.separator + "oias" + File.separator
					+ lista3.get(i).getCaminhoPasta());
			ResponseBuilder response = Response.ok((Object) file);
			// response.header("Content-Disposition", "attachment; nomeArquivo=" +
			// lista3.get(i).getCaminhoPasta());

			return response.status(Status.OK).header("nomeArquivo", lista3.get(i).getCaminhoPasta()).build();
		}
		return Response.status(Response.Status.OK).type("application/json").entity("Nenhum arquivo encontrado!")
				.build();
	}
}
