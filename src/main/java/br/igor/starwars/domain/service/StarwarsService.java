package br.igor.starwars.domain.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StarwarsService {

	private final String API_PLANET_STARWARS = "https://swapi.co/api/planets";
	
	public int buscarQuantidadeAparicoesFilmes(String nomePlaneta) {
		CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		ResponseEntity<String> response = 
				restTemplate.getForEntity(API_PLANET_STARWARS + "?search=" + nomePlaneta, String.class);
		
		return extrairQuantidade(response.getBody());
	}

	private int extrairQuantidade(String body) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNodeRoot = objectMapper.readTree(body);
			JsonNode jsonResults = jsonNodeRoot.findValue("results");
			JsonNode jsonFilms = jsonResults.findValue("films");
			if(jsonFilms != null) {
				String filmes = jsonFilms.toString();
				List<String> listaFilmes = objectMapper.readValue(filmes, new TypeReference<List<String>>() {});
				return listaFilmes.size();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
}
