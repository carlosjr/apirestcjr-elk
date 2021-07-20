package br.com.cjr.apirest.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.cjr.apirest.model.Demo;
import br.com.cjr.apirest.model.dto.DemoDto;
import br.com.cjr.apirest.repository.DemoRepository;

@Service
public class DemoService {

	private DemoRepository demoRepository;
	
	private DemoNotificacaoService demoNotificacaoService;
	
	@Autowired
	public DemoService(DemoRepository demoRepository, DemoNotificacaoService demoNotificacaoService) {
	    this.demoRepository = demoRepository;
	    this.demoNotificacaoService = demoNotificacaoService;
	}
	

	public BigDecimal pontucaoPorNome(String nome) {
		if(nome == null) {
			throw new IllegalArgumentException("Erro para pontuação precisa ser passado um nome!");
		}

		List<Demo> demos = demoRepository.findByNome(nome);
		
		if(!ObjectUtils.isEmpty(demos)){
			demoNotificacaoService.notificarComMensagem(demos.get(0));
			return demos.get(0).getPontuacao();
		}
		
		return BigDecimal.ZERO;
	}

	public void inativarPorPontuacao() {
		List<Demo> demos = demoRepository.findAll();
		demos.forEach(d -> {
			if(d.getPontuacao().compareTo(new BigDecimal(100)) < 0){
				d.setStatus(false);
				demoRepository.save(d);
			}
		});
		
		demoNotificacaoService.notificarInativacao(demos);
	}
	
	
	public void dobrarPontuacaoPorNome(String nome) {
		List<Demo> demos = demoRepository.findByNome(nome);
		
		if(!ObjectUtils.isEmpty(demos)){
			Demo demo = demos.get(0);
			
			BigDecimal dobro = demo.getPontuacao().multiply(new BigDecimal(2));
			demo.setPontuacao(dobro);
			
			demoRepository.save(demo);
		}
	}

	public Page<DemoDto> listarDemosDoBanco(String nome, Pageable paginacao) {
		Page<Demo> demos;
		if(nome == null) {
			demos = demoRepository.findAll(paginacao);
		} else {
			demos = demoRepository.buscarPorNome(nome, paginacao);
			
		}
		
		class PhoneNumber {
			public void name() {
				System.out.println("qq");
			}
		}
		
		PhoneNumber number = new PhoneNumber();
		number.name();
		
		return DemoDto.converter(demos);
	}
	
	
}
