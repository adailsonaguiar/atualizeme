package atualizeme.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import atualizeme.model.Arquivo;
import atualizeme.test.*;

import java.io.FileOutputStream;

@Path("update")
public class Update {

	private static String caminho = System.getProperty("user.home") + File.separator + "oias" + File.separator;

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
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet("http://localhost:8080/atualizeme/api/update/get");
			// getRequest.addHeader("accept", "application/zip");
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			/// long l = response.getEntity().getContent()getContentLength();
			String filePath = response.getLastHeader("filePath").getValue();
			System.out.println(filePath);

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				//System.out.println(output);
				
			}

			httpClient.getConnectionManager().shutdown();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		*/
		
		
		
		
		 Client client = ClientBuilder.newClient();
	        String url = "http://localhost:8080/atualizeme/api/update/get";
	        Response response = client.target(url).request().get();
	        String location = response.getHeaderString("filePath")+" - Novo";
	        FileOutputStream out = new FileOutputStream(location);
	        InputStream is = (InputStream)response.getEntity();
	        int len = 0;
	        byte[] buffer = new byte[4096];
	        while((len = is.read(buffer)) != -1) {
	            out.write(buffer, 0, len);
	        }
	        out.flush();
	        out.close();
	        is.close();

	}

	public static void savefile(URI sourceuri) {
		String sourceFilename = sourceuri.getPath();
		String destinationFilename = "C:\\Users\\adailsonacj\\Downloads\\abc.zip";

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFilename));
			bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
			byte[] buf = new byte[1024];
			bis.read(buf);
			do {
				bos.write(buf);
			} while (bis.read(buf) != -1);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveFile(InputStream uploadedInputStream, String uploadedFileLocation) throws Exception {
		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
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
			System.out.println(lista3.get(i).getPathFile());
			File file = new File(lista3.get(i).getPathFile());
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + lista3.get(i).getPathFile());

//			return response.build();
			return response.status(Status.OK).header("filePath", lista3.get(i).getPathFile()).build();
		}
		return Response.status(Response.Status.OK).type("application/json")
				.entity("Nenhum arquivo encontrado!").build();
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
