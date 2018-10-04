package controller;

import model.Agente;
import model.Tabuleiro;

public class LabirintoController {
	
	public static void main(String[] args) {
		Tabuleiro tabuleiro = new Tabuleiro();
		tabuleiro.imprimeTabuleiro();
		System.out.println("------------------------------");
		tabuleiro.imprimeTabuleiroVisivelPeloAgente();
		System.out.println("------------------------------");
		
		Agente agente = (Agente) tabuleiro.getAgente();
		agente.ativarModoAutonomo();
	}
}
