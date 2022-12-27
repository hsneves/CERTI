package br.com.hsneves.certi.test.rest;

import java.io.IOException;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.com.hsneves.certi.test.CertiTestApplication;
import br.com.hsneves.certi.test.config.CertiCtaJpaTestConfig;
import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.utils.Constants;

/**
 * Testes unitários do controlador REST
 * 
 * @author Henrique Neves
 *
 */
@RunWith(SpringRunner.class)
@PropertySource("application-test.properties")
@SpringBootTest(classes = { CertiTestApplication.class, CertiCtaJpaTestConfig.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles(Constants.PROFILE_TEST)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // importante para os testes executarem em ordem
public class PokemonControllerTest {

	private String url;

	@Before
	public void setUp() {
		this.url = "http://localhost:3001/certi/rest";
	}

	@Test
	public void test1GetAll() throws IOException, ParseException {

		Response response = get(this.url + "/pokemon/all");

		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getMimeType(), "application/json");
		Assert.assertNotNull(response.getContent());

		Gson gson = new GsonBuilder().create();
		List<Pokeball> pokeballs = gson.fromJson(response.getContent(), new TypeToken<List<Pokeball>>() {
		}.getType());

		Assert.assertNotNull(pokeballs);
		Assert.assertEquals(151, pokeballs.size());

	}

	@Test
	public void test2GetPokeballs() throws IOException, ParseException {

		Response response = get(this.url + "/pokemon/pokeballs");

		Assert.assertNotNull(response);
		Assert.assertEquals(200, response.getStatusCode());
		Assert.assertEquals("application/json", response.getMimeType());
		Assert.assertNotNull(response.getContent());

		Gson gson = new GsonBuilder().create();
		List<Pokeball> pokeballs = gson.fromJson(response.getContent(), new TypeToken<List<Pokeball>>() {
		}.getType());

		Assert.assertNotNull(pokeballs);
		Assert.assertEquals(pokeballs.size(), 0);

	}

	@Test
	public void test4ThrowPokeball() throws IOException, ParseException {

		// captura o pidgey
		Response response = get(this.url + "/pokemon/catch/pidgey");

		Assert.assertNotNull(response);
		Assert.assertEquals(200, response.getStatusCode());
		Assert.assertEquals("application/json", response.getMimeType());
		Assert.assertNotNull(response.getContent());

		// recupera as pokebolas após a captura do pidgey
		response = get(this.url + "/pokemon/pokeballs");

		Assert.assertNotNull(response);
		Assert.assertEquals(200, response.getStatusCode());
		Assert.assertEquals("application/json", response.getMimeType());
		Assert.assertNotNull(response.getContent());

		Gson gson = new GsonBuilder().create();
		List<Pokeball> pokeballs = gson.fromJson(response.getContent(), new TypeToken<List<Pokeball>>() {
		}.getType());

		Assert.assertNotNull(pokeballs);
		Assert.assertEquals(pokeballs.size(), 1); // deve ter 1 elemento
		Assert.assertEquals(pokeballs.get(0).getPokemon().getName(), "Pidgey"); // deve ser o pidgey

		// volta pokebola vazia
		response = get(this.url + "/pokemon/catch/xxx");

		Assert.assertNotNull(response);
		Assert.assertEquals(200, response.getStatusCode());
		Assert.assertEquals("application/json", response.getMimeType());
		Assert.assertNotNull(response.getContent());

		// recupera as pokebolas após a pokebola voltar vazia
		response = get(this.url + "/pokemon/pokeballs");

		Assert.assertNotNull(response);
		Assert.assertEquals(200, response.getStatusCode());
		Assert.assertEquals("application/json", response.getMimeType());
		Assert.assertNotNull(response.getContent());

		pokeballs = gson.fromJson(response.getContent(), new TypeToken<List<Pokeball>>() {
		}.getType());

		Assert.assertNotNull(pokeballs);
		Assert.assertEquals(1, pokeballs.size()); // deve ter 1 elemento
	}

	/**
	 * Executa um GET na API REST
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private Response get(String url) throws IOException, ParseException {

		Response result = null;

		HttpGet httpGet = new HttpGet(url);

		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			try (@SuppressWarnings("deprecation")
			CloseableHttpResponse response = httpclient.execute(httpGet)) {

				int statusCode = response.getCode(); // 200
				HttpEntity entity = response.getEntity();
				String mimeType = response.getEntity().getContentType();
				String resultContent = EntityUtils.toString(entity);

				result = new Response(statusCode, mimeType, resultContent);
			}
		}

		return result;
	}

	/**
	 * Classe interna para encapsular o response dos gets para a API REST
	 * 
	 * @author Henrique Neves
	 *
	 */
	private class Response {
		private final int statusCode;
		private final String mimeType;
		private final String content;

		public Response(int statusCode, String mimeType, String content) {
			super();
			this.statusCode = statusCode;
			this.mimeType = mimeType;
			this.content = content;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public String getMimeType() {
			return mimeType;
		}

		public String getContent() {
			return content;
		}

	}
}
