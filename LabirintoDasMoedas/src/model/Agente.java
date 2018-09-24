package model;

import java.util.ArrayList;
import java.util.List;

public class Agente extends Objeto {
	
	private List<SacoDeMoedas> sacosDeMoedas;
	private int pontuacao;
	
	public Agente(int posicaoX, int posicaoY) {
		super(posicaoX, posicaoY, TipoDeObjeto.AGENTE);
		this.sacosDeMoedas = new ArrayList<SacoDeMoedas>();
		this.pontuacao = 0;
	}

	public List<SacoDeMoedas> getSacosDeMoedas() {
		return sacosDeMoedas;
	}

	public void setSacosDeMoedas(List<SacoDeMoedas> sacosDeMoedas) {
		this.sacosDeMoedas = sacosDeMoedas;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}
	
	@Override
	public String toString() {
		return "A";
	}
}
