package model;

import java.util.ArrayList;
import java.util.List;

public class Bau extends Objeto {
	
	private List<SacoDeMoedas> sacosDeMoeda;

	public Bau(int posicaoX, int posicaoY) {
		super(posicaoX, posicaoY, TipoDeObjeto.BAU);
		this.sacosDeMoeda = new ArrayList<SacoDeMoedas>();
	}

	public List<SacoDeMoedas> getSacosDeMoeda() {
		return sacosDeMoeda;
	}

	public void setSacosDeMoeda(List<SacoDeMoedas> sacosDeMoeda) {
		this.sacosDeMoeda = sacosDeMoeda;
	}
	
	public int getQuantidadeTotalDeMoedas() {
		int quantidadeTotal = 0;

		for (SacoDeMoedas saco : sacosDeMoeda) {
			quantidadeTotal += saco.getQuantidadeDeMoedas();
		}
		
		return quantidadeTotal;
	}
	
	@Override
	public String toString() {
		return "B";
	}

}
