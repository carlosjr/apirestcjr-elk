package br.com.cjr.apirest.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.cjr.apirest.model.Pontuacao;
import br.com.cjr.apirest.model.dto.PontuacaoDto;
import br.com.cjr.apirest.repository.PontuacaoRepository;

@Service
public class PontuacaoService {

	@Autowired
	private PontuacaoRepository pontuacaoRepository;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
//	@Autowired
//	public PontuacaoService(PontuacaoRepository pontuacaoRepository, NotificacaoService notificacaoService) {
//	    this.pontuacaoRepository = pontuacaoRepository;
//	    this.notificacaoService = notificacaoService;
//	}
//	

	public BigDecimal pontucaoPorNome(String nome) {
		if(nome == null) {
			throw new IllegalArgumentException("Erro para pontuação precisa ser passado um nome!");
		}

		List<Pontuacao> pontuacaos = pontuacaoRepository.findByNome(nome);
		
		if(!ObjectUtils.isEmpty(pontuacaos)){
			notificacaoService.notificarComMensagem(pontuacaos.get(0));
			return pontuacaos.get(0).getPontuacao();
		}
		
		return BigDecimal.ZERO;
	}

	public void inativarPorPontuacao() {
		List<Pontuacao> pontuacaos = pontuacaoRepository.findAll();
		pontuacaos.forEach(d -> {
			if(d.getPontuacao().compareTo(new BigDecimal(100)) < 0){
				d.setStatus(false);
				pontuacaoRepository.save(d);
			}
		});
		
		notificacaoService.notificarInativacao(pontuacaos);
	}
	
	
	public void dobrarPontuacaoPorNome(String nome) {
		List<Pontuacao> pontuacaos = pontuacaoRepository.findByNome(nome);
		
		if(!ObjectUtils.isEmpty(pontuacaos)){
			Pontuacao pontuacao = pontuacaos.get(0);
			
			BigDecimal dobro = pontuacao.getPontuacao().multiply(new BigDecimal(2));
			pontuacao.setPontuacao(dobro);
			
			pontuacaoRepository.save(pontuacao);
		}
	}

	public Page<PontuacaoDto> listarPontuacoesDoBanco(String nome, Pageable paginacao) {
		Page<Pontuacao> pontuacaos;
		if(nome == null) {
			pontuacaos = pontuacaoRepository.findAll(paginacao);
		} else {
			pontuacaos = pontuacaoRepository.buscarPorNome(nome, paginacao);
			
		}
		
		return PontuacaoDto.converter(pontuacaos);
	}
	
	
}
