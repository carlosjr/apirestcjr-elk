package br.com.cjr.apirest.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.cjr.apirest.model.Pontuacao;
import br.com.cjr.apirest.model.dto.PontuacaoDto;
import br.com.cjr.apirest.model.request.PontuacaoRequest;
import br.com.cjr.apirest.repository.PontuacaoRepository;
import br.com.cjr.apirest.service.PontuacaoService;

@RestController
@RequestMapping("/pontuacao")
public class PontuacaoRestController {
	
	private static final Logger LOG = LogManager.getLogger(PontuacaoRestController.class);

	@Autowired
	private PontuacaoRepository pontuacaoRepository;
	
	@Autowired
	private PontuacaoService pontuacaoService;
	
	@GetMapping("/verificarPontuacao")
	public BigDecimal verificarPontucaoPorNome(@RequestParam(required = false) String nome) {
		try {
			return pontuacaoService.pontucaoPorNome(nome);
		} catch (Exception e) {
			LOG.error(String.format("Erro em verificarPontucaoPorNome: %s", e.getMessage()));
			throw new SecurityException("Erro em verificarPontucaoPorNome",e);
		}
	}
	
	@GetMapping("/inativarPontuacao")
	public void inativarPorPontuacao() {
		pontuacaoService.inativarPorPontuacao();
	}
	
	// Paginacao manual
	@GetMapping
	public Page<PontuacaoDto> listarPontuacoes(@RequestParam(required = false) String nome, 
			@RequestParam int pagina, @RequestParam int qtd, @RequestParam(required = false) String ordenacao) {
		Pageable paginacao = PageRequest.of(pagina, qtd, Direction.DESC, ordenacao);
		return pontuacaoService.listarPontuacoesDoBanco(nome, paginacao);
	}
	
	// Com Pageable, habilitar no main o web suport, e passar parametros page, size, sort(id,asc)
	@GetMapping("/page-default")
	public Page<PontuacaoDto> listarPontuacoes(@RequestParam(required = false) String nome, 
			@PageableDefault(sort="id", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
		return pontuacaoService.listarPontuacoesDoBanco(nome, paginacao);
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<PontuacaoDto> cadastrarPontuacoes(@RequestBody PontuacaoRequest pontuacaoRequest, UriComponentsBuilder uriComponentsBuilder) {
		Pontuacao pontuacao = new Pontuacao(pontuacaoRequest);
		pontuacao.setDataCriacao(LocalDateTime.now());
		
		pontuacaoRepository.save(pontuacao);
		
		URI uri = uriComponentsBuilder.path("/pontuacao/{id}").buildAndExpand(pontuacao.getId()).toUri();
		return ResponseEntity.created(uri).body(new PontuacaoDto(pontuacao));
	}
	
}
