package br.com.cjr.apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.cjr.apirest.controller.DemoRestController;

@SpringBootTest
class DemoTest {

	@Autowired
	DemoRestController demoRestController;
	
	@BeforeEach
	public void inicializar() {
		//TODO inicializar variaveis sera usado antes de cada metodo
	}
	
	@AfterEach
	public void finalizar() {
		//TODO realiza apos cada de cada metodo
	}
	
	@BeforeAll
	public static void iniciarAntesDeTudo() {
		System.out.println("Inicio de testes ");
	}
	
	@AfterAll
	public static void finalizarDepoisDeTudo() {
		System.out.println("Fim de testes ");
	}
	
	@Test
	void pontucaoPorNomeDeveria100() {
		BigDecimal pontuacao =  demoRestController.verificarPontucaoPorNome("Demo");
		assertEquals(new BigDecimal("100.00"), pontuacao);
	}
	
	@Test
	void pontucaoPorNomeDeveria50() {
		BigDecimal pontuacao =  demoRestController.verificarPontucaoPorNome("Carlos");
		assertEquals(new BigDecimal("50.00"), pontuacao);
	}
	
	@Test
	void pontucaoPorNomeDeveriaZero() {
		BigDecimal pontuacao =  demoRestController.verificarPontucaoPorNome("Flamengo");
		assertEquals(BigDecimal.ZERO, pontuacao);
	}
	
	@Test
	void pontucaoPorNomeNaoDeveriaSerNuloAssert() {
		assertThrows(IllegalArgumentException.class, () -> demoRestController.verificarPontucaoPorNome(null));
	}
	
	@Test
	void pontucaoPorNomeNaoDeveriaSerNuloTryCathValidaMensagem() {
		try {
			demoRestController.verificarPontucaoPorNome(null);
			fail("Não deu exception de negocio!");
		} catch (Exception e) {
			assertEquals("Erro para pontuação precisa ser passado um nome!", e.getMessage());
		}
	}

}
