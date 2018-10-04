package model;

import java.util.ArrayList;
import java.util.List;

public class Configuracao {

	private int tamanhoX;
	private int tamanhoY;
	private int quantidadeDeBaus;
	private int quantidadeDeParedesInternas;
	private int tamanhoDasParedesInternas;
	private int quantidadeDeBuracos;
	private int quantidadeDeSacosDeMoedas;
	private int quantidadeMaximaDeMoedas;
	private int quantidadeMinimaDeMoedasNecessariasEmCadaBau;
	private int alcanceVisaoAgente;
	private int sleepTime;
	private int maximoDeTentativas;
	
	private List<EstrategiaMuro> estrategiasMuro;

	public Configuracao() {
		this.estrategiasMuro = new ArrayList<>();
		configuracaoPadrao();
	}

	public void configuracaoPadrao() {
		this.tamanhoX = 10;
		this.alcanceVisaoAgente = 2;
		this.sleepTime = 400;
		this.maximoDeTentativas = 10;
		
		configuracaoProporcionalAoTamanhoX();
		criaEstrategiasMuro();
	}

	public void configuracaoProporcionalAoTamanhoX() {
		this.tamanhoY = tamanhoX;
		this.quantidadeDeBaus = (tamanhoX + tamanhoY) / 5;
		this.quantidadeDeParedesInternas = (tamanhoX + tamanhoY) / 5;
		this.tamanhoDasParedesInternas = (tamanhoX + tamanhoY) / 4;
		this.quantidadeDeBuracos = (tamanhoX + tamanhoY) / 4;
		this.quantidadeDeSacosDeMoedas = quantidadeDeBaus * 4;
		this.quantidadeMaximaDeMoedas = quantidadeDeSacosDeMoedas * 50;
		this.quantidadeMinimaDeMoedasNecessariasEmCadaBau = 40;
	}

	public void criaEstrategiasMuro() {
		estrategiasMuro.add(new EstrategiaMuro(0, 0, Orientacao.VERTICAL));
		estrategiasMuro.add(new EstrategiaMuro(this.tamanhoX / 10, this.tamanhoX / 5, Orientacao.VERTICAL));
		estrategiasMuro.add(new EstrategiaMuro(this.tamanhoX / 10, this.tamanhoX / 5, Orientacao.HORIZONTAL));
		estrategiasMuro.add(new EstrategiaMuro((this.tamanhoX * 9) / 10, 0, Orientacao.HORIZONTAL));
		estrategiasMuro.add(new EstrategiaMuro((this.tamanhoX * 3) / 10, (this.tamanhoX * 6) / 10, Orientacao.VERTICAL));
		estrategiasMuro.add(new EstrategiaMuro((this.tamanhoX * 3) / 10, (this.tamanhoX * 4) / 10, Orientacao.VERTICAL));
		estrategiasMuro.add(new EstrategiaMuro(0, (this.tamanhoY * 7) / 10, Orientacao.VERTICAL));
		estrategiasMuro.add(new EstrategiaMuro((this.tamanhoX * 7) / 10, 0, Orientacao.HORIZONTAL));
		estrategiasMuro.add(new EstrategiaMuro((this.tamanhoX * 3) / 10, 0, Orientacao.VERTICAL));
		estrategiasMuro.add(new EstrategiaMuro(this.tamanhoX / 5, (this.tamanhoY * 7) / 10, Orientacao.VERTICAL));
		estrategiasMuro.add(new EstrategiaMuro((this.tamanhoX * 9) / 10, this.tamanhoX / 5, Orientacao.HORIZONTAL));
		estrategiasMuro.add(new EstrategiaMuro(0, this.tamanhoX / 5, Orientacao.HORIZONTAL));
	}

	public int getTamanhoX() {
		return tamanhoX;
	}

	public void setTamanhoX(int tamanhoX) {
		this.tamanhoX = tamanhoX;
	}

	public int getTamanhoY() {
		return tamanhoY;
	}

	public void setTamanhoY(int tamanhoY) {
		this.tamanhoY = tamanhoY;
	}

	public int getQuantidadeDeBaus() {
		return quantidadeDeBaus;
	}

	public void setQuantidadeDeBaus(int quantidadeDeBaus) {
		this.quantidadeDeBaus = quantidadeDeBaus;
	}

	public int getQuantidadeDeParedesInternas() {
		return quantidadeDeParedesInternas;
	}

	public void setQuantidadeDeParedesInternas(int quantidadeDeParedesInternas) {
		this.quantidadeDeParedesInternas = quantidadeDeParedesInternas;
	}

	public int getTamanhoDasParedesInternas() {
		return tamanhoDasParedesInternas;
	}

	public void setTamanhoDasParedesInternas(int tamanhoDasParedesInternas) {
		this.tamanhoDasParedesInternas = tamanhoDasParedesInternas;
	}

	public int getQuantidadeDeBuracos() {
		return quantidadeDeBuracos;
	}

	public void setQuantidadeDeBuracos(int quantidadeDeBuracos) {
		this.quantidadeDeBuracos = quantidadeDeBuracos;
	}

	public int getQuantidadeDeSacosDeMoedas() {
		return quantidadeDeSacosDeMoedas;
	}

	public void setQuantidadeDeSacosDeMoedas(int quantidadeDeSacosDeMoedas) {
		this.quantidadeDeSacosDeMoedas = quantidadeDeSacosDeMoedas;
	}

	public List<EstrategiaMuro> getEstrategiasMuro() {
		return estrategiasMuro;
	}

	public void setEstrategiasMuro(List<EstrategiaMuro> estrategiasMuro) {
		this.estrategiasMuro = estrategiasMuro;
	}
	
	public int getQuantidadeMaximaDeMoedas() {
		return quantidadeMaximaDeMoedas;
	}

	public void setQuantidadeMaximaDeMoedas(int quantidadeMaximaDeMoedas) {
		this.quantidadeMaximaDeMoedas = quantidadeMaximaDeMoedas;
	}
	
	public int getQuantidadeMinimaDeMoedasNecessariasEmCadaBau() {
		return quantidadeMinimaDeMoedasNecessariasEmCadaBau;
	}

	public void setQuantidadeMinimaDeMoedasNecessariasEmCadaBau(int quantidadeMinimaDeMoedasNecessariasEmCadaBau) {
		this.quantidadeMinimaDeMoedasNecessariasEmCadaBau = quantidadeMinimaDeMoedasNecessariasEmCadaBau;
	}

	public int getAlcanceVisaoAgente() {
		return alcanceVisaoAgente;
	}

	public void setAlcanceVisaoAgente(int alcanceVisaoAgente) {
		this.alcanceVisaoAgente = alcanceVisaoAgente;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getMaximoDeTentativas() {
		return maximoDeTentativas;
	}

	public void setMaximoDeTentativas(int maximoDeTentativas) {
		this.maximoDeTentativas = maximoDeTentativas;
	}
	
}
