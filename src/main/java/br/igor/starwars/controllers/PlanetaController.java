package br.igor.starwars.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.igor.starwars.domain.models.Planeta;
import br.igor.starwars.domain.service.PlanetaService;
import br.igor.starwars.infra.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/planetas")
public class PlanetaController {
	
	private PlanetaService planetaService;
	private ApplicationEventPublisher publisher;
	
	@Autowired
	public PlanetaController(PlanetaService planetaService, ApplicationEventPublisher publisher) {
		this.planetaService = planetaService;
		this.publisher = publisher;
	}

	@PostMapping
	public ResponseEntity<Planeta> adicionarPlaneta(@Valid @RequestBody Planeta planeta, HttpServletResponse response) {
		Planeta planetaAdicionado = planetaService.salvarPlaneta(planeta); 
		publisher.publishEvent(new RecursoCriadoEvent(this, response, planetaAdicionado.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(planetaAdicionado);
	}
	
	@GetMapping
	public List<Planeta> listarPlanetas() {
		return this.planetaService.buscarPlanetas();
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<Object> buscarPlanetaPorNome(@PathVariable String nome) {
		Optional<Planeta> planetaEncontrado = this.planetaService.buscarPlanetaPorNome(nome);
		if(planetaEncontrado.isPresent())
			return ResponseEntity.ok(planetaEncontrado.get());
		else 
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Planeta> buscarPlanetaPorId(@PathVariable Long id) {
		Optional<Planeta> planetaEncontrado = this.planetaService.buscarPlanetaPorId(id);
		if(planetaEncontrado.isPresent())
			return ResponseEntity.ok(planetaEncontrado.get());
		else 
			return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removerPlaneta(@PathVariable Long id) {
		Optional<Planeta> planetaEncontrado = this.planetaService.buscarPlanetaPorId(id);
		if(planetaEncontrado.isPresent()) {
			this.planetaService.deletarPlaneta(id);
			return ResponseEntity.noContent().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
			
	}

}
