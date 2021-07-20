package br.com.cjr.apirest.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.cjr.apirest.model.Demo;

public class DemoDto {

	private String nome;
	private BigDecimal pontuacao;
	private Boolean status;
	private LocalDateTime dataCriacao;
	
	
	public DemoDto(Demo demo) {
		this.nome = demo.getNome();
		this.dataCriacao = demo.getDataCriacao();
		this.pontuacao = demo.getPontuacao();
		this.status = demo.getStatus();
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

	public static List<DemoDto> converter(List<Demo> demos) {
		return demos.stream().map(DemoDto::new).collect(Collectors.toList());
	}
	
	public static Page<DemoDto> converter(Page<Demo> demos) {
		return demos.map(DemoDto::new);
	}
}
