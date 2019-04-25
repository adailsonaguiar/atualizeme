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
import java.net.URISyntaxException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.core.Response.ResponseBuilder;

import atualizeme.model.ArquivoTxt;
import atualizeme.test.*;

import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("update")
public class Update {

	private static String caminhoAplicacao = System.getProperty("user.home") + File.separator + "oias" + File.separator;

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, URISyntaxException {

		ArquivoMD5 md5 = new ArquivoMD5();
		md5.setNome("MD5.txt");
		md5.setPastaAplicação(caminhoAplicacao);
		md5.arquivomd5(md5.getPastaAplicação(), md5.getNome());

		List<ArquivoTxt> listacliente = md5.readFile(md5.getPastaAplicação() + "2" + md5.getNome());

		Client client = ClientBuilder.newClient();
		String url = "http://localhost:8080/atualizeme/api/update/get";
		Response response = client.target(url).request().post(Entity.entity(listacliente, MediaType.APPLICATION_JSON));
		String location = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "oias"
				+ File.separator + response.getHeaderString("nomeArquivo");
//		FileOutputStream out = new FileOutputStream(location);
//		InputStream is = (InputStream) response.getEntity();
//		int len = 0;
//		byte[] buffer = new byte[4096];
//		while ((len = is.read(buffer)) != -1) {
//			out.write(buffer, 0, len);
//		}
//		out.flush();
//		out.close();
//		is.close();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String teste() {
		return "teste realizado com sucesso.";
	}

	@javax.ws.rs.POST
	@Path("/get/{clienteLista}")
	@Consumes(MediaType.APPLICATION_JSON)
	@javax.ws.rs.Produces({ "application/json" })
	public Response getArquivos(@PathParam("clienteLista") String clienteLista)
			throws NoSuchAlgorithmException, IOException {

		System.out.println(clienteLista);
//		ArquivoMD5 md5 = new ArquivoMD5();
//		md5.setNome("MD5.txt");
//		md5.setPastaAplicação(caminhoAplicacao);
//		md5.arquivomd5(md5.getPastaAplicação(), md5.getNome());
//
//		List<ArquivoTxt> lista = md5.readFile(md5.getPastaAplicação() + md5.getNome());
//		
//
//		List<ArquivoTxt> lista3 = md5.comparaArquivosMD5(lista, listacliente);
//
//		for (int i = 0; i < lista3.size(); i++) {
//			File file = new File(System.getProperty("user.home") + File.separator + "oias" + File.separator
//					+ lista3.get(i).getCaminhoPasta());
//			ResponseBuilder response = Response.ok((Object) file);
//			return response.status(Status.PARTIAL_CONTENT).header("nomeArquivo", lista3.get(i).getCaminhoPasta())
//					.build();
//		}
		return Response.status(Response.Status.OK).type("application/json").entity("Nada para Atualizar!").build();
	}
}
