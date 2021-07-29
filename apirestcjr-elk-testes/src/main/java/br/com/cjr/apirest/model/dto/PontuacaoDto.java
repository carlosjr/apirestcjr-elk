package br.com.cjr.apirest.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.cjr.apirest.model.Pontuacao;

public class PontuacaoDto {

	private Long id;
	private String nome;
	private BigDecimal pontuacao;
	private Boolean status;
	private LocalDateTime dataCriacao;
	
	
	public PontuacaoDto(Pontuacao pontuacao) {
		this.id = pontuacao.getId();
		this.nome = pontuacao.getNome();
		this.dataCriacao = pontuacao.getDataCriacao();
		this.pontuacao = pontuacao.getPontuacao();
		this.status = pontuacao.getStatus();
	}
	
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public BigDecimal getPontuacao() {
		return pontuacao;
	}

	public Boolean getStatus() {
		return status;
	}

	public static List<PontuacaoDto> converter(List<Pontuacao> pontuacaos) {
		return pontuacaos.stream().map(PontuacaoDto::new).collect(Collectors.toList());
	}
	
	public static Page<PontuacaoDto> converter(Page<Pontuacao> pontuacaos) {
		return pontuacaos.map(PontuacaoDto::new);
	}
	
}
