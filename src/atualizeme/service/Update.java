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

import atualizeme.atualizacao.*;
import atualizeme.model.Arquivo;

import com.google.gson.Gson;

@Path("update")
public class Update {

	private static String caminhoAplicacao = "C:\\xampp\\htdocs\\sicap\\";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String teste() {
		return "teste realizado com sucesso.";
	}

	@javax.ws.rs.GET
	@Path("/arquivoverificacao/")
	@Consumes(MediaType.APPLICATION_JSON)
	@javax.ws.rs.Produces({ "application/json" })
	public Response baixarArquivoVerificacao() throws NoSuchAlgorithmException, IOException {
		ArquivosAtualizacao arquivoAtualizacao = new ArquivosAtualizacao();
		arquivoAtualizacao.setNome("MD5.txt");
		arquivoAtualizacao.setPastaAplicacao(caminhoAplicacao);
		arquivoAtualizacao.arquivomd5(arquivoAtualizacao.getPastaAplicacao(), arquivoAtualizacao.getNome());
		File file = new File(caminhoAplicacao + arquivoAtualizacao.getNome());
		ResponseBuilder response = Response.ok((Object) file);
		return response.status(Status.OK).build();
	}

	@javax.ws.rs.GET
	@Path("/verificaratualizacao/")
	@Consumes(MediaType.APPLICATION_JSON)
	@javax.ws.rs.Produces({ "application/json" })
	public Response verificarAtualizacao() throws NoSuchAlgorithmException, IOException {
		ArquivosAtualizacao arquivoAtualizacao = new ArquivosAtualizacao();
		arquivoAtualizacao.setNome("MD5.txt");
		arquivoAtualizacao.setPastaAplicacao(caminhoAplicacao);
		arquivoAtualizacao.arquivomd5(arquivoAtualizacao.getPastaAplicacao(), arquivoAtualizacao.getNome());

		List<Arquivo> listaServidor = arquivoAtualizacao
				.readFile(arquivoAtualizacao.getPastaAplicacao() + arquivoAtualizacao.getNome());
		String json = new Gson().toJson(listaServidor);

		return Response.ok(json).build();
	}

	@javax.ws.rs.GET
	@Path("/getarquivo/{caminhoPasta}/{nome}")
	@Consumes(MediaType.APPLICATION_JSON)
	@javax.ws.rs.Produces({ "application/json" })
	public Response enviarArquivo(@PathParam("caminhoPasta") String caminhoPasta, @PathParam("nome") String nome)
			throws NoSuchAlgorithmException, IOException {
		byte[] caminhoBytes = Base64.getDecoder().decode(caminhoPasta);
		String caminho = new String(caminhoBytes);
		byte[] nomeBytes = Base64.getDecoder().decode(nome);
		String nomeArq = new String(nomeBytes);
		File file = new File(caminhoAplicacao + caminho + nomeArq);
		ResponseBuilder response = Response.ok((Object) file);
		return response.status(Status.OK).build();
	}
}
