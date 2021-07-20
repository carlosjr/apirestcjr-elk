package br.com.cjr.apirest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.cjr.apirest.model.Demo;

@Service
public class DemoNotificacaoService {

	public void notificarInativacao(List<Demo> demos) {
		demos.stream()
			 .filter(d-> !d.getStatus())
			 .forEach(d-> 
			 	enviarNotificacao(String.format("Notificando inativação para %s", d.getNome()))
			 );
	}
	
	public void notificarComMensagem(Demo demo) {
		String mensagem = "";
		if(Boolean.TRUE.equals(demo.getStatus())) {
			mensagem = String.format("Usuario sua pontuação é %s!", demo.getPontuacao());
		} else {
			mensagem = "Usuario sua pontuação será zerada!";
		}
		enviarNotificacao(mensagem);
	}
	
	private void enviarNotificacao(String msg) {
		System.out.println(msg);
	}
}
