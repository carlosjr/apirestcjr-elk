package br.com.cjr.apirest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.cjr.apirest.model.Pontuacao;

@Service
public class NotificacaoService {

	public void notificarInativacao(List<Pontuacao> pontuacaos) {
		pontuacaos.stream()
			 .filter(d-> !d.getStatus())
			 .forEach(d-> 
			 	enviarNotificacao(String.format("Notificando inativação para %s", d.getNome()))
			 );
	}
	
	public void notificarComMensagem(Pontuacao pontuacao) {
		String mensagem = "";
		if(Boolean.TRUE.equals(pontuacao.getStatus())) {
			mensagem = String.format("Usuario sua pontuação é %s!", pontuacao.getPontuacao());
		} else {
			mensagem = "Usuario sua pontuação será zerada!";
		}
		enviarNotificacao(mensagem);
	}
	
	private void enviarNotificacao(String msg) {
		System.out.println(msg);
	}
}
