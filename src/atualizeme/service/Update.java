package atualizeme.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import java.net.URISyntaxException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import atualizeme.model.ArquivoTxt;
import atualizeme.test.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Path("update")
public class Update {

	private static String caminhoAplicacao = System.getProperty("user.home") + File.separator + "oias" + File.separator;

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, URISyntaxException {
		boolean end = false;
		while (!end) {
			ArquivoMD5 md5 = new ArquivoMD5();
			md5.setNome("MD5.txt");
			md5.setPastaAplicacao(System.getProperty("user.home") + File.separator + "Downloads" + File.separator
					+ "oias" + File.separator);
			md5.arquivomd5(md5.getPastaAplicacao(), md5.getNome());

			List<ArquivoTxt> listacliente = md5.readFile(md5.getPastaAplicacao() + md5.getNome());

			String json = new Gson().toJson(listacliente);
			String encodedString = Base64.getEncoder().encodeToString(json.getBytes());

			Client client = ClientBuilder.newClient();
			Response response = client.target("http://localhost:8080/atualizeme/api/update/get").path(encodedString)
					.request().get();

			if (response.getStatus() == 200) {
				end = true;
				System.out.println("Tudo atualizado!");
			} else {

				String location = System.getProperty("user.home") + File.separator + "Downloads" + File.separator
						+ "oias" + File.separator + response.getHeaderString("nomeArquivo");
				System.out.println(location);

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
		}

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String teste() {
		return "teste realizado com sucesso.";
	}

	@javax.ws.rs.GET
	@Path("/get/{listaCliente}")
	@Consumes(MediaType.APPLICATION_JSON)
	@javax.ws.rs.Produces({ "application/json" })
	public Response getArquivos(@PathParam("listaCliente") String listaCliente)
			throws NoSuchAlgorithmException, IOException {

		byte[] decodedBytes = Base64.getDecoder().decode(listaCliente);
		String decodedString = new String(decodedBytes);
		Type listType = new TypeToken<ArrayList<ArquivoTxt>>() {
		}.getType();
		List<ArquivoTxt> lista2 = new Gson().fromJson(decodedString, listType);
		ArquivoMD5 md5 = new ArquivoMD5();
		md5.setNome("MD5.txt");
		md5.setPastaAplicacao(caminhoAplicacao);
		md5.arquivomd5(md5.getPastaAplicacao(), md5.getNome());

		List<ArquivoTxt> lista = md5.readFile(md5.getPastaAplicacao() + md5.getNome());

		List<ArquivoTxt> lista3 = md5.comparaArquivosMD5(lista, lista2);

		for (int i = 0; i < lista3.size(); i++) {
			File file = new File(System.getProperty("user.home") + File.separator + "oias" + File.separator
					+ lista3.get(i).getCaminhoPasta());
			ResponseBuilder response = Response.ok((Object) file);
			System.out.println(lista3.get(i).getCaminhoPasta());
			return response.status(Status.PARTIAL_CONTENT).header("nomeArquivo", lista3.get(i).getCaminhoPasta())
					.build();
		}
		return Response.status(Response.Status.OK).type("application/json").entity("Nada para Atualizar!").build();
	}
}
