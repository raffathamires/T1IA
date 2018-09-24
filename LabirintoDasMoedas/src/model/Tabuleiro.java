package model;

import java.util.List;
import java.util.Random;

public class Tabuleiro {

	private Configuracao configuracao;
	private Objeto[][] tabuleiro;

	public Tabuleiro() {
		this.configuracao = new Configuracao();
		this.tabuleiro = new Objeto[configuracao.getTamanhoX()][configuracao.getTamanhoY()];

		inicializaTabuleiro();
	}

	public Objeto[][] getTabuleiro() {
		return tabuleiro;
	}

	private void inicializaTabuleiro() {
		for (int i = 0; i < configuracao.getTamanhoX(); i++) {
			for (int j = 0; j < configuracao.getTamanhoY(); j++) {
				if (tabuleiro[i][j] == null) {
					tabuleiro[i][j] = new Livre(i, j);
				}
			}
		}

		criaParedao();
		criaPorta();
		criaBaus();
		criaParedesInternas();
		criaBuracos();
		criaSacosDeMoedas(); 
		criaAgente();
	}

	private void criaParedao() {
		for (int i = 0; i < configuracao.getTamanhoY(); i++) {
			int posicaoY = configuracao.getTamanhoY() - 1;
			tabuleiro[i][posicaoY] = new Muro(i, posicaoY);
		}
	}

	private void criaPorta() {
		Random random = new Random();
		int posicaoX = random.nextInt(configuracao.getTamanhoX());
		int posicaoY = configuracao.getTamanhoY() - 1;

		tabuleiro[posicaoX][posicaoY] = new Porta(posicaoX, posicaoY);
	}

	private void criaBaus() {
		int bausCriados = 0;
		Random random = new Random();

		while (bausCriados < configuracao.getQuantidadeDeBaus()) {
			int posicaoX = random.nextInt(configuracao.getTamanhoX());
			int posicaoY = configuracao.getTamanhoY() - 2;

			if (tabuleiro[posicaoX][posicaoY].getTipo() == TipoDeObjeto.LIVRE
					&& tabuleiro[posicaoX][posicaoY + 1].getTipo() == TipoDeObjeto.MURO) {
				tabuleiro[posicaoX][posicaoY] = new Bau(posicaoX, posicaoY);
				bausCriados++;
			}
		}
	}

	public void criaParedesInternas() {
		Random random = new Random();
		List<EstrategiaMuro> estrategiasMuro = configuracao.getEstrategiasMuro();

		int paredesCriadas = 0;
		while (paredesCriadas < configuracao.getQuantidadeDeParedesInternas()) {
			EstrategiaMuro estrategia = estrategiasMuro.get(random.nextInt(estrategiasMuro.size()));

			if (tabuleiro[estrategia.getPosicaoX()][estrategia.getPosicaoY()].tipo == TipoDeObjeto.LIVRE) {
				for (int i = 0; i < configuracao.getTamanhoDasParedesInternas(); i++) {
					if (estrategia.getOrientacao() == Orientacao.VERTICAL) {
						tabuleiro[estrategia.getPosicaoX() + i][estrategia.getPosicaoY()] = new Muro(estrategia.getPosicaoX() + i, estrategia.getPosicaoY());
					} else {
						tabuleiro[estrategia.getPosicaoX()][estrategia.getPosicaoY() + i] = new Muro(estrategia.getPosicaoX(), estrategia.getPosicaoY() + i);
					}
				}
				paredesCriadas++;
			}
		}
	}
	
	private void criaBuracos() {
		int buracosCriados = 0;
		Random random = new Random();

		while (buracosCriados < configuracao.getQuantidadeDeBuracos()) {
			int posicaoRandomX = random.nextInt(configuracao.getTamanhoX());
			int posicaoRandomY = random.nextInt(configuracao.getTamanhoY() - 1);

			if (tabuleiro[posicaoRandomX][posicaoRandomY].getTipo() == TipoDeObjeto.LIVRE 
					&& !existeBuracoVizinho(posicaoRandomX, posicaoRandomY)
					&& !estaEmUmCanto(posicaoRandomX, posicaoRandomY)) {
				tabuleiro[posicaoRandomX][posicaoRandomY] = new Buraco(posicaoRandomX, posicaoRandomY);
				buracosCriados++;
			}
		}
	}
	
	private boolean estaEmUmCanto(int posicaoRandomX, int posicaoRandomY) {
		if (posicaoRandomX == 0 && posicaoRandomY == 0 
				|| posicaoRandomX == configuracao.getTamanhoX() - 1 && posicaoRandomY == 0
				|| posicaoRandomX == configuracao.getTamanhoX() - 1 && posicaoRandomY == configuracao.getTamanhoY() - 2
				|| posicaoRandomX == 0 && posicaoRandomY == configuracao.getTamanhoY() - 2) {
			return true;
		}
		
		if (posicaoRandomX >= 0 && posicaoRandomX < configuracao.getTamanhoX() - 1) {
			if (tabuleiro[posicaoRandomX+1][posicaoRandomY].getTipo() == TipoDeObjeto.MURO) {
				if (posicaoRandomY > 0) {
					if (tabuleiro[posicaoRandomX][posicaoRandomY-1].getTipo() == TipoDeObjeto.MURO) {
						return true;
					}
				}
				if (posicaoRandomY < configuracao.getTamanhoY() - 1) {
					if (tabuleiro[posicaoRandomX][posicaoRandomY+1].getTipo() == TipoDeObjeto.MURO) {
						return true;
					}
				}
			}
		}
		
		if (posicaoRandomX <= configuracao.getTamanhoX() - 1 && posicaoRandomX > 0) {
			if (tabuleiro[posicaoRandomX-1][posicaoRandomY].getTipo() == TipoDeObjeto.MURO) {
				if (posicaoRandomY > 0) {
					if (tabuleiro[posicaoRandomX][posicaoRandomY-1].getTipo() == TipoDeObjeto.MURO) {
						return true;
					}
				}
				if (posicaoRandomY < configuracao.getTamanhoY() - 1) {
					if (tabuleiro[posicaoRandomX][posicaoRandomY+1].getTipo() == TipoDeObjeto.MURO) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

	private boolean existeBuracoVizinho(int posicaoX, int posicaoY) {
		if (posicaoX == 0) {
			if (tabuleiro[posicaoX + 1][posicaoY].getTipo() == TipoDeObjeto.BURACO) {
				return true;
			}
		} else if (posicaoX == configuracao.getTamanhoX() - 1) {
			if (tabuleiro[posicaoX - 2][posicaoY].getTipo() == TipoDeObjeto.BURACO) {
				return true;
			}
		} else {
			if (tabuleiro[posicaoX + 1][posicaoY].getTipo() == TipoDeObjeto.BURACO) {
				return true;
			}
			if (tabuleiro[posicaoX - 1][posicaoY].getTipo() == TipoDeObjeto.BURACO) {
				return true;
			}
		}

		if (posicaoY != 0) {
			if (tabuleiro[posicaoX][posicaoY - 1].getTipo() == TipoDeObjeto.BURACO) {
				return true;
			}
		}
		if (tabuleiro[posicaoX][posicaoY + 1].getTipo() == TipoDeObjeto.BURACO) {
			return true;
		}

		return false;
	}
	
	private void criaSacosDeMoedas() {
		int sacosCriados = 0;
		Random random = new Random();
		int quantidadeDeMoedasRestantesParaDistribuicao = configuracao.getQuantidadeMaximaDeMoedas();
		int quantidadeDeSacosDeMoeda = configuracao.getQuantidadeDeSacosDeMoedas();
		int mediaDeMoedasPorSaco = quantidadeDeMoedasRestantesParaDistribuicao / quantidadeDeSacosDeMoeda;

		while (sacosCriados < configuracao.getQuantidadeDeSacosDeMoedas()) {
			
			int posicaoRandomX = random.nextInt(configuracao.getTamanhoX());
			int posicaoRandomY = random.nextInt(configuracao.getTamanhoY() - 1);

			if (tabuleiro[posicaoRandomX][posicaoRandomY].getTipo() == TipoDeObjeto.LIVRE) {
				tabuleiro[posicaoRandomX][posicaoRandomY] = new SacoDeMoedas(posicaoRandomX, posicaoRandomY, random.nextInt(mediaDeMoedasPorSaco));
				sacosCriados++;
			}
		}
	}

	private void criaAgente() {
		Random random = new Random();
		boolean agenteCriado = false;

		while (!agenteCriado) {
			int posicaoRandomX = random.nextInt(configuracao.getTamanhoX());
			int posicaoRandomY = random.nextInt(configuracao.getTamanhoY() - 1);

			if (tabuleiro[posicaoRandomX][posicaoRandomY].getTipo() == TipoDeObjeto.LIVRE) {
				tabuleiro[posicaoRandomX][posicaoRandomY] = new Agente(posicaoRandomX, posicaoRandomY);
				agenteCriado = true;
			}
		}
	}

	public void imprimeTabuleiro() {
		StringBuilder sb;

		for (int i = 0; i < configuracao.getTamanhoX(); i++) {
			sb = new StringBuilder();

			for (int j = 0; j < configuracao.getTamanhoY(); j++) {
				sb.append(" ");
				sb.append(insereEspacos(this.tabuleiro[i][j].toString(), 2));
			}
			System.out.println(sb.toString());
		}
	}

	private String insereEspacos(String text, int num) {
		while (text.length() < num) {
			text = " " + text;
		}

		return text;
	}

}
