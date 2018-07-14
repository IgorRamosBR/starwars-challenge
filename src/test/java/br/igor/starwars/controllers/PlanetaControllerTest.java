package br.igor.starwars.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.igor.starwars.StarwarsChallengeApplicationTest;
import br.igor.starwars.controllers.PlanetaController;
import br.igor.starwars.domain.models.Planeta;
import br.igor.starwars.domain.repository.PlanetaRepository;

public class PlanetaControllerTest extends StarwarsChallengeApplicationTest {
	
	private MockMvc mockMvc;
	
	@Autowired
	private PlanetaController planetaController;
	
	@Autowired
	private PlanetaRepository repository;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(planetaController).build();
		clear();
	}

	@Test
	public void deve_retornar_todos_os_planetas() throws Exception {
		salvaPlaneta();
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/planetas"))
			.andExpect(MockMvcResultMatchers.status().isOk());
		
		clear();
	}
	
	@Test
	public void deve_retornar_planeta_por_id() throws Exception {
		Planeta planeta = salvaPlaneta();
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/planetas/{id}", planeta.getId()))
			.andExpect(status().isOk());
		
		clear();
	}
	
	@Test
	public void deve_retornar_planeta_por_nome() throws Exception {
		Planeta planeta = salvaPlaneta();
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/planetas/nome/{nome}", planeta.getNome()))
			.andExpect(status().isOk());
		
		clear();
	}
	
	@Test
	public void deve_retornar_not_found_na_busca_por_id() throws Exception {
		Planeta planeta = salvaPlaneta();
		clear();
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/planetas/{id}", planeta.getId()))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void deve_retornar_not_found_na_busca_por_nome() throws Exception {
		Planeta planeta = salvaPlaneta();
		clear();
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/planetas/nome/{nome}", planeta.getNome()))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void deve_retornar_no_content_ao_deletar_planeta() throws Exception {
		Planeta planeta = salvaPlaneta();
		
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/planetas/{id}", planeta.getId()))
			.andExpect(status().isNoContent());
	}
	
	@Test
	public void deve_retornar_not_found_ao_deletar_planeta() throws Exception {
		Planeta planeta = salvaPlaneta();
		clear();
		
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/planetas/{id}", planeta.getId()))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void deve_retornar_created_ao_salvar_planeta() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/planetas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(criaPlaneta())))
				.andExpect(status().isCreated());
	}
	
	@After
	public void shutdown() {
		clear();
	}
		
	private Planeta salvaPlaneta() {
		Planeta planeta1 = criaPlaneta();
		
		return repository.save(planeta1);
	}

	private Planeta criaPlaneta() {
		Planeta planeta1 = new Planeta();
		planeta1.setNome("Venus");
		planeta1.setClima("Temperado");
		planeta1.setTerreno("Floresta");
		planeta1.setAparicoesFilmes(0);
		return planeta1;
	}
	
	private void clear() {
		repository.deleteAll();
	}
}
