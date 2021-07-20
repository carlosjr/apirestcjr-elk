package br.com.cjr.apirest.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.cjr.apirest.model.Demo;
import br.com.cjr.apirest.model.dto.DemoDto;
import br.com.cjr.apirest.model.form.DemoForm;
import br.com.cjr.apirest.repository.DemoRepository;
import br.com.cjr.apirest.service.DemoService;

@RestController
@RequestMapping("/demo")
public class DemoRestController {

	@Autowired
	private DemoRepository demoRepository;
	
	@Autowired
	private DemoService demoService;
	
	@GetMapping("/verificarPontuacao")
	public BigDecimal verificarPontucaoPorNome(@RequestParam(required = false) String nome) {
		return demoService.pontucaoPorNome(nome);
	}
	
	@GetMapping("/inativarPontuacao")
	public void inativarPorPontuacao() {
		demoService.inativarPorPontuacao();
	}
	
	// Paginacao manual
	@GetMapping
	public Page<DemoDto> listarDemos(@RequestParam(required = false) String nome, 
			@RequestParam int pagina, @RequestParam int qtd, @RequestParam(required = false) String ordenacao) {
		Pageable paginacao = PageRequest.of(pagina, qtd, Direction.DESC, ordenacao);
		return demoService.listarDemosDoBanco(nome, paginacao);
	}
	
	// Com Pageable, habilitar no main o web suport, e passar parametros page, size, sort(id,asc)
	@GetMapping("/page-default")
	public Page<DemoDto> listarDemos(@RequestParam(required = false) String nome, 
			@PageableDefault(sort="id", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
		return demoService.listarDemosDoBanco(nome, paginacao);
	}
	
	@PostMapping("/banco")
	public ResponseEntity<DemoDto> cadastrarDemos(@RequestBody DemoForm demoForm, UriComponentsBuilder uriComponentsBuilder) {
		Demo demo = new Demo(demoForm);
		demo.setDataCriacao(LocalDateTime.now());
		demoRepository.save(demo);
		
		URI uri = uriComponentsBuilder.path("/demo/{id}").buildAndExpand(demo.getId()).toUri();
		return ResponseEntity.created(uri).body(new DemoDto(demo));
	}
	
}
