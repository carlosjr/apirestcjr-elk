package br.com.cjr.apirest.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.cjr.apirest.model.Pontuacao;

public interface PontuacaoRepository extends JpaRepository<Pontuacao, Long>{
	
	List<Pontuacao> findByNome(String nome);

	@Query("SELECT d FROM Pontuacao d WHERE d.nome = :nome")
	Page<Pontuacao> buscarPorNome(@Param("nome") String nome, Pageable paginacao);

}
