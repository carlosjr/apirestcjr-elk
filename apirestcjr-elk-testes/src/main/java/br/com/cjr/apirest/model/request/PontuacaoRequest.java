package br.com.cjr.apirest.model.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PontuacaoRequest {
	
	private Long id;
	
	@NotNull @NotEmpty
	private String nome;
	private BigDecimal pontuacao;
	private Boolean status;
	
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

	@Override
	public String toString() {
		return "PontuacaoRequest [id=" + id + ", nome=" + nome + ", pontuacao=" + pontuacao + ", status=" + status
				+ "]";
	}
	
}
