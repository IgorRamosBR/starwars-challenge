package br.igor.starwars.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.igor.starwars.domain.models.Planeta;

public interface PlanetaRepository extends JpaRepository<Planeta, Long>{

	public Optional<Planeta> findFirstByNome(String nome);

}
