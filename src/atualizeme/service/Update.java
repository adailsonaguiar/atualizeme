package atualizeme.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import atualizeme.model.ArquivoTxt;
import atualizeme.test.*;

import com.google.gson.Gson;

@Path("update")
public class Update {

	private static String caminhoAplicacao = System.getProperty("user.home") + File.separator + "oias" + File.separator;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String teste() {
		return "teste realizado com sucesso.";
	}

	@javax.ws.rs.GET
	@Path("/verificar/")
	@Consumes(MediaType.APPLICATION_JSON)
	@javax.ws.rs.Produces({ "application/json" })
	public Response verificaAtualizacao() throws NoSuchAlgorithmException, IOException {
		ArquivoMD5 md5 = new ArquivoMD5();
		md5.setNome("MD5.txt");
		md5.setPastaAplicacao(caminhoAplicacao);
		md5.arquivomd5(md5.getPastaAplicacao(), md5.getNome());

		List<ArquivoTxt> listaServidor = md5.readFile(md5.getPastaAplicacao() + md5.getNome());
		String json = new Gson().toJson(listaServidor);
		String strListaServidor = Base64.getEncoder().encodeToString(json.getBytes());

		ResponseBuilder response = Response.ok();

		return response.status(Status.OK).header("listaServidor", strListaServidor).build();
	}

	@javax.ws.rs.GET
	@Path("/getArquivo/{caminhoPasta}")
	@Consumes(MediaType.APPLICATION_JSON)
	@javax.ws.rs.Produces({ "application/json" })
	public Response enviaArquivo(@PathParam("caminhoPasta") String caminhoPasta)
			throws NoSuchAlgorithmException, IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(caminhoPasta);
		String decodedString = new String(decodedBytes);
		File file = new File(caminhoAplicacao + decodedString);
		ResponseBuilder response = Response.ok((Object) file);
		return response.status(Status.OK).build();
	}
}
