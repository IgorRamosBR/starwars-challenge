package br.igor.starwars.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.igor.starwars.domain.models.Planeta;
import br.igor.starwars.domain.repository.PlanetaRepository;

@Service
public class PlanetaService {
	
	private PlanetaRepository planetaRepository;
	private StarwarsService starwarsService;
	
	@Autowired
	public PlanetaService(PlanetaRepository planetaRepository, StarwarsService starwarsService) {
		this.planetaRepository = planetaRepository;
		this.starwarsService = starwarsService;
	}

	public Planeta salvarPlaneta(Planeta planeta) {
		planeta.setAparicoesFilmes(starwarsService.buscarQuantidadeAparicoesFilmes(planeta.getNome()));
		return planetaRepository.save(planeta);
	}

	public List<Planeta> buscarPlanetas() {
		return this.planetaRepository.findAll();
	}

	public Optional<Planeta> buscarPlanetaPorNome(String nome) {
		return this.planetaRepository.findFirstByNome(nome);
	}

	public Optional<Planeta> buscarPlanetaPorId(Long id) {
		return this.planetaRepository.findById(id);
	}

	public void deletarPlaneta(Long id) {
		this.planetaRepository.deleteById(id);
	}

}
