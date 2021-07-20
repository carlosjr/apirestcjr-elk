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

import br.com.cjr.apirest.model.Demo;
import br.com.cjr.apirest.repository.DemoRepository;
import br.com.cjr.apirest.service.DemoNotificacaoService;
import br.com.cjr.apirest.service.DemoService;


@SpringBootTest
class DemoMokitoTest {

	@InjectMocks
	private DemoService demoService;
	
	@Mock
	private DemoRepository demoRepository;
	
	@Mock
	private DemoNotificacaoService demoNotificacaoService;
	
	@Captor
	private ArgumentCaptor<Demo> captor;
	
	private List<Demo> demos;
	
	@BeforeEach
	public void inicializar() {
		this.demos = demos();
	}
	
	@Test
	void deveriaInativarPorPontuacao() {
		
		Mockito.when(demoRepository.findAll()).thenReturn(this.demos);
		
		demoService.inativarPorPontuacao();
		
		Demo demo = this.demos.get(0);
		assertFalse(demo.getStatus());
		assertTrue(demo.getPontuacao().compareTo(new BigDecimal(100)) < 0);
		
		Mockito.verify(demoRepository).save(demo);
	}
	
	@Test
	void deveriaNotificarInativados() {
		
		Mockito.when(demoRepository.findAll()).thenReturn(this.demos);
		demoService.inativarPorPontuacao();
		Mockito.verify(demoNotificacaoService).notificarInativacao(demos);
	}

	@Test
	void naoDeveriaNotificarInativados() {
		
		Mockito.when(demoRepository.findAll()).thenReturn(this.demos);
		Mockito.when(demoRepository.save(Mockito.any())).thenThrow(RuntimeException.class);
		
		try {
			demoService.inativarPorPontuacao();
			Mockito.verifyNoInteractions(demoNotificacaoService);
		} catch (Exception e) {}
		
	}
	
	@Test
	void deveriaEnviarMensagensDeAcordoComStatus() {
		Mockito.when(demoRepository.findByNome(Mockito.any())).thenReturn(this.demos);
		
		demoService.dobrarPontuacaoPorNome("Carlos");
		
		Mockito.verify(demoRepository).save(captor.capture()); 

		Demo demo = captor.getValue();
		assertEquals(new BigDecimal(100), demo.getPontuacao());
		assertEquals(Boolean.TRUE, demo.getStatus());
		
	}
	
	private List<Demo> demos() {
		Demo demo = new Demo();
		demo.setNome("Carlos");
		demo.setStatus(true);
		demo.setPontuacao(new BigDecimal(50));
		
		Demo demo2 = new Demo();
		demo2.setNome("Demo");
		demo2.setStatus(true);
		demo2.setPontuacao(new BigDecimal(100));
		
		return Arrays.asList(demo, demo2);
	}
}
