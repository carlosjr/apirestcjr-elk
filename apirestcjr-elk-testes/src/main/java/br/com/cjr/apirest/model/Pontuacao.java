package br.com.cjr.apirest.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.cjr.apirest.model.request.PontuacaoRequest;

@Entity
public class Pontuacao {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private BigDecimal pontuacao;
	private Boolean status;
	private LocalDateTime dataCriacao;
	
	public Pontuacao() {
	}
	
	public Pontuacao(PontuacaoRequest pontuacaoRequest) {
		this.id = pontuacaoRequest.getId();
		this.nome = pontuacaoRequest.getNome();
		this.status = pontuacaoRequest.getStatus();
		this.pontuacao = pontuacaoRequest.getPontuacao() ;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(BigDecimal pontuacao) {
		this.pontuacao = pontuacao;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
}
