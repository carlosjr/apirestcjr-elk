package br.com.cjr.apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.cjr.apirest.model.Pontuacao;
import br.com.cjr.apirest.repository.PontuacaoRepository;
import br.com.cjr.apirest.service.NotificacaoService;
import br.com.cjr.apirest.service.PontuacaoService;


@SpringBootTest
class PontuacaoMokitoTest {

	@InjectMocks
	private PontuacaoService pontuacaoService;
	
	@Mock
	private PontuacaoRepository pontuacaoRepository;
	
	@Mock
	private NotificacaoService notificacaoService;
	
	@Captor
	private ArgumentCaptor<Pontuacao> captor;
	
	private List<Pontuacao> pontuacaos;
	
	@BeforeEach
	public void inicializar() {
		this.pontuacaos = pontuacaos();
	}
	
	@Test
	void deveriaInativarPorPontuacao() {
		
		Mockito.when(pontuacaoRepository.findAll()).thenReturn(this.pontuacaos);
		
		pontuacaoService.inativarPorPontuacao();
		
		Pontuacao pontuacao = this.pontuacaos.get(0);
		assertFalse(pontuacao.getStatus());
		assertTrue(pontuacao.getPontuacao().compareTo(new BigDecimal(100)) < 0);
		
		Mockito.verify(pontuacaoRepository).save(pontuacao);
	}
	
	@Test
	void deveriaNotificarInativados() {
		
		Mockito.when(pontuacaoRepository.findAll()).thenReturn(this.pontuacaos);
		pontuacaoService.inativarPorPontuacao();
		Mockito.verify(notificacaoService).notificarInativacao(pontuacaos);
	}

	@Test
	void naoDeveriaNotificarInativados() {
		
		Mockito.when(pontuacaoRepository.findAll()).thenReturn(this.pontuacaos);
		Mockito.when(pontuacaoRepository.save(Mockito.any())).thenThrow(RuntimeException.class);
		
		try {
			pontuacaoService.inativarPorPontuacao();
			Mockito.verifyNoInteractions(notificacaoService);
		} catch (Exception e) {}
		
	}
	
	@Test
	void deveriaEnviarMensagensDeAcordoComStatus() {
		Mockito.when(pontuacaoRepository.findByNome(Mockito.any())).thenReturn(this.pontuacaos);
		
		pontuacaoService.dobrarPontuacaoPorNome("Carlos");
		
		Mockito.verify(pontuacaoRepository).save(captor.capture()); 

		Pontuacao pontuacao = captor.getValue();
		assertEquals(new BigDecimal(100), pontuacao.getPontuacao());
		assertEquals(Boolean.TRUE, pontuacao.getStatus());
		
	}
	
	private List<Pontuacao> pontuacaos() {
		Pontuacao pontuacao = new Pontuacao();
		pontuacao.setNome("Carlos");
		pontuacao.setStatus(true);
		pontuacao.setPontuacao(new BigDecimal(50));
		
		Pontuacao demo2 = new Pontuacao();
		demo2.setNome("Pontuacao");
		demo2.setStatus(true);
		demo2.setPontuacao(new BigDecimal(100));
		
		return Arrays.asList(pontuacao, demo2);
	}
}
