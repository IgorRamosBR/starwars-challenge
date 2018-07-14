package br.igor.starwars.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.igor.starwars.StarwarsChallengeApplicationTest;
import br.igor.starwars.domain.models.Planeta;
import br.igor.starwars.domain.repository.PlanetaRepository;

public class PlanetServiceTest extends StarwarsChallengeApplicationTest{
	
	@Autowired
	private PlanetaService planetaService;
	
	@Autowired
	private PlanetaRepository repository;
	
	@Before
	public void setup() {
		clear();
	}
	
	@Test
	public void deve_salvar_planeta_com_as_aparicoes_corretas() {
		Planeta planeta1 = new Planeta();
		planeta1.setNome("Alderaan");
		planeta1.setClima("Temperado");
		planeta1.setTerreno("Montanhas");
		
		Planeta planetaSalvo1 = planetaService.salvarPlaneta(planeta1);
		
		Planeta planeta2 = new Planeta();
		planeta2.setNome("Yavin IV");
		planeta2.setClima("Tropical");
		planeta2.setTerreno("Selva");
		
		Planeta planetaSalvo2 = planetaService.salvarPlaneta(planeta2);
		
		assertEquals(2, planetaSalvo1.getAparicoesFilmes());
		assertEquals(1, planetaSalvo2.getAparicoesFilmes());
	}
	
	@Test
	public void deve_salvar_planeta_com_zero_aparicoes_se_nao_for_do_starwars() {
		Planeta planeta1 = new Planeta();
		planeta1.setNome("Marte");
		planeta1.setClima("Seco");
		planeta1.setTerreno("Vermelho");
		
		Planeta planetaSalvo1 = planetaService.salvarPlaneta(planeta1);
		
		Planeta planeta2 = new Planeta();
		planeta2.setNome("Plutão");
		planeta2.setClima("Frio");
		planeta2.setTerreno("Gelo");
		
		Planeta planetaSalvo2 = planetaService.salvarPlaneta(planeta2);
		
		assertEquals(0, planetaSalvo1.getAparicoesFilmes());
		assertEquals(0, planetaSalvo2.getAparicoesFilmes());
	}

	@Test
	public void deve_retornar_todos_os_planetas() {
		clear();
		
		Planeta planeta1 = new Planeta();
		planeta1.setNome("Saturno");
		planeta1.setClima("Temperado");
		planeta1.setTerreno("Montanhas");
		planeta1.setAparicoesFilmes(0);
		
		repository.save(planeta1);
		
		Planeta planeta2 = new Planeta();
		planeta2.setNome("Netuno");
		planeta2.setClima("Tropical");
		planeta2.setTerreno("Selva");
		planeta2.setAparicoesFilmes(0);
		
		repository.save(planeta2);
		
		assertEquals(2, (repository.findAll()).size());
	}
	
	@Test
	public void deve_retornar_planeta_por_id() {
		Planeta planeta1 = new Planeta();
		planeta1.setNome("Saturno");
		planeta1.setClima("Temperado");
		planeta1.setTerreno("Montanhas");
		planeta1.setAparicoesFilmes(0);
		
		Planeta planetaSalvo = repository.save(planeta1);
		Planeta planetaBuscado = (planetaService.buscarPlanetaPorId(planetaSalvo.getId())).get();
		assertTrue(planetaBuscado.equals(planetaSalvo));
	}
	
	@Test
	public void deve_retornar_planeta_por_nome() {
		Planeta planeta1 = new Planeta();
		planeta1.setNome("Terra");
		planeta1.setClima("Tropical");
		planeta1.setTerreno("Agua");
		planeta1.setAparicoesFilmes(0);
		
		Planeta planetaSalvo = repository.save(planeta1);
		Planeta planetaBuscado = (planetaService.buscarPlanetaPorNome(planetaSalvo.getNome())).get();
		assertTrue(planetaBuscado.equals(planetaSalvo));
	}

	@Test
	public void deve_deletar_planeta() {
		Planeta planeta1 = new Planeta();
		planeta1.setNome("Mercúrio");
		planeta1.setClima("Quente");
		planeta1.setTerreno("Vermelho");
		planeta1.setAparicoesFilmes(0);
		
		Planeta planetaSalvo = repository.save(planeta1);
		planetaService.deletarPlaneta(planetaSalvo.getId());
		
		Optional<Planeta> planetaBuscado = repository.findById(planetaSalvo.getId());
		assertFalse(planetaBuscado.isPresent());
		
	}
	
	@After
 	public void shutdown() {
		clear();
	}
	
	private void clear() {
		repository.deleteAll();
	}
	
}
