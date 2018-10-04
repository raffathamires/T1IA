package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Tabuleiro {

	private Configuracao configuracao;
	private Objeto[][] tabuleiro;
	private Agente agente;
	private Queue<Objeto> objetosParaRetornarAoTabuleiro;
	private int iteracoes;

	public Tabuleiro() {
		this.configuracao = new Configuracao();
		this.tabuleiro = new Objeto[configuracao.getTamanhoX()][configuracao.getTamanhoY()];
		this.objetosParaRetornarAoTabuleiro = new PriorityQueue<Objeto>();
		this.iteracoes = 0;
		
		inicializaTabuleiro();
	}

	private Objeto[][] getTabuleiro() {
		return tabuleiro;
	}

	public Objeto getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public Configuracao getConfiguracao() {
		return configuracao;
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
		mapearObjetosNoAlcanceDoAgente();

		/*
		 * List<Coordenadas> coordenadas = aStarSearch(agente.getCoordenadas(), new
		 * Coordenadas(0, 0));
		 * 
		 * if (coordenadas != null) { for (Coordenadas c : coordenadas) {
		 * System.out.println(c.getPosicaoX() + " - " + c.getPosicaoY()); } }
		 */
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

	private void criaParedesInternas() {
		Random random = new Random();
		List<EstrategiaMuro> estrategiasMuro = configuracao.getEstrategiasMuro();

		int paredesCriadas = 0;
		while (paredesCriadas < configuracao.getQuantidadeDeParedesInternas()) {
			EstrategiaMuro estrategia = estrategiasMuro.get(random.nextInt(estrategiasMuro.size()));

			if (tabuleiro[estrategia.getPosicaoX()][estrategia.getPosicaoY()].tipo == TipoDeObjeto.LIVRE) {
				for (int i = 0; i < configuracao.getTamanhoDasParedesInternas(); i++) {
					if (estrategia.getOrientacao() == Orientacao.VERTICAL) {
						tabuleiro[estrategia.getPosicaoX() + i][estrategia.getPosicaoY()] = new Muro(
								estrategia.getPosicaoX() + i, estrategia.getPosicaoY());
					} else {
						tabuleiro[estrategia.getPosicaoX()][estrategia.getPosicaoY() + i] = new Muro(
								estrategia.getPosicaoX(), estrategia.getPosicaoY() + i);
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
			if (tabuleiro[posicaoRandomX + 1][posicaoRandomY].getTipo() == TipoDeObjeto.MURO) {
				if (posicaoRandomY > 0) {
					if (tabuleiro[posicaoRandomX][posicaoRandomY - 1].getTipo() == TipoDeObjeto.MURO) {
						return true;
					}
				}
				if (posicaoRandomY < configuracao.getTamanhoY() - 1) {
					if (tabuleiro[posicaoRandomX][posicaoRandomY + 1].getTipo() == TipoDeObjeto.MURO) {
						return true;
					}
				}
			}
		}

		if (posicaoRandomX <= configuracao.getTamanhoX() - 1 && posicaoRandomX > 0) {
			if (tabuleiro[posicaoRandomX - 1][posicaoRandomY].getTipo() == TipoDeObjeto.MURO) {
				if (posicaoRandomY > 0) {
					if (tabuleiro[posicaoRandomX][posicaoRandomY - 1].getTipo() == TipoDeObjeto.MURO) {
						return true;
					}
				}
				if (posicaoRandomY < configuracao.getTamanhoY() - 1) {
					if (tabuleiro[posicaoRandomX][posicaoRandomY + 1].getTipo() == TipoDeObjeto.MURO) {
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
		
		if (posicaoY == 0) {
			if (tabuleiro[posicaoX][posicaoY + 1].getTipo() == TipoDeObjeto.BURACO) {
				return true;
			}
		} else if (posicaoY == configuracao.getTamanhoY() - 2) {
			if (tabuleiro[posicaoX][posicaoY - 3].getTipo() == TipoDeObjeto.BURACO) {
				return true;
			}
		} else {
			if (tabuleiro[posicaoX][posicaoY + 1].getTipo() == TipoDeObjeto.BURACO) {
				return true;
			}
			if (tabuleiro[posicaoX][posicaoY - 1].getTipo() == TipoDeObjeto.BURACO) {
				return true;
			}
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
				tabuleiro[posicaoRandomX][posicaoRandomY] = new SacoDeMoedas(posicaoRandomX, posicaoRandomY, 10/*random.nextInt(mediaDeMoedasPorSaco)*/);
				sacosCriados++;
			}
		}
	}

	private void criaAgente() {
		Random random = new Random();

		while (getAgente() == null) {
			int posicaoRandomX = random.nextInt(configuracao.getTamanhoX());
			int posicaoRandomY = random.nextInt(configuracao.getTamanhoY() - 1);

			if (tabuleiro[posicaoRandomX][posicaoRandomY].getTipo() == TipoDeObjeto.LIVRE) {
				tabuleiro[posicaoRandomX][posicaoRandomY] = new Agente(posicaoRandomX, posicaoRandomY, this);
				this.agente = (Agente) tabuleiro[posicaoRandomX][posicaoRandomY];
				getAgente().setConhecido(true);
			}
		}
	}

	public void imprimeTabuleiro() {
		StringBuilder sb = new StringBuilder();
		int espacos = 2;

		for (int i = 0; i < configuracao.getTamanhoX(); i++) {
			for (int j = 0; j < configuracao.getTamanhoY(); j++) {
				sb.append(" ");
				sb.append(insereEspacos(this.tabuleiro[i][j].toString(), espacos));
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
		
		if (agente != null) {
			imprimirPontuacao();		
		}
	}
	
	public void imprimirValor(Coordenadas c) {
		System.out.println("Coordenadas: " + c.toString());
	}
	
	public void imprimirPontuacao() {
		StringBuilder sb = new StringBuilder();
		sb.append("Pontuação Total: " + agente.getPontuacao() + " pontos \n");
		
		sb.append("Sacos de Moedas: ");
		for (int i = 1; i <= agente.getSacosDeMoedas().size(); i++) {
			sb.append("(" + i + ": " + agente.getSacosDeMoedas().get(i-1).getQuantidadeDeMoedas() + " moedas)");
		}
		
		System.out.println(sb.toString());
		
		sb = new StringBuilder();
		sb.append("Baús Conhecidos: ");
		ArrayList<Bau> baus = new ArrayList<>();
		baus.addAll(agente.getBaus());
		for (int i = 1; i <= baus.size(); i++) {
			sb.append("(" + i + ": " + baus.get(i-1).getQuantidadeTotalDeMoedas() + " moedas)");
		}
		System.out.println(sb.toString());
		
		System.out.println("Iterações: " + iteracoes);
		
	}

	public void imprimeTabuleiroVisivelPeloAgente() {
		StringBuilder sb;
		sb = new StringBuilder();

		for (int i = 0; i < configuracao.getTamanhoX(); i++) {
			for (int j = 0; j < configuracao.getTamanhoY(); j++) {
				if (this.tabuleiro[i][j].conhecido) {
					sb.append(" ");
					sb.append(insereEspacos(this.tabuleiro[i][j].toString(), 2));
				} else {
					sb.append(" ");
					sb.append(insereEspacos(" ", 2));
				}
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
	}

	private String insereEspacos(String text, int num) {
		while (text.length() < num) {
			text = " " + text;
		}

		return text;
	}

	private Objeto getObjeto(int x, int y) {
		if (x >= 0 && x <= configuracao.getTamanhoX() - 1 && y >= 0 && y <= configuracao.getTamanhoY() - 1) {
			return tabuleiro[x][y];
		}
		return null;
	}

	public Objeto getObjetoPelasCoordenadas(Coordenadas coordenadas) {
		return this.tabuleiro[coordenadas.getPosicaoX()][coordenadas.getPosicaoY()];
	}

	public Map<Objeto, Double> initializeAllToInfinity() {
		Map<Objeto, Double> distances = new HashMap<>();

		for (int i = 0; i < configuracao.getTamanhoX(); i++) {
			for (int j = 0; j < configuracao.getTamanhoY(); j++) {
				distances.put(tabuleiro[i][j], Double.POSITIVE_INFINITY);
			}
		}

		return distances;
	}

	private void mapearObjetosNoAlcanceDoAgente() {
		int posX = agente.getCoordenadas().getPosicaoX();
		int posY = agente.getCoordenadas().getPosicaoY();
		int alcance = configuracao.getAlcanceVisaoAgente();

		// Superior
		Objeto atual = agente;
		Objeto superior = null;
		for (int i = 1; i <= alcance; i++) {
			if ((posX - i) >= 0) {
				superior = tabuleiro[(posX - i)][posY];
				superior.setConhecido(true);
				atual.setObjetoNorte(superior);
				superior.setObjetoSul(atual);
				
				if (superior.getTipo() == TipoDeObjeto.PORTA) {
					agente.setPorta((Porta) superior);
				}
				if (superior.getTipo() == TipoDeObjeto.BAU) {
					agente.getBaus().add((Bau) superior);
				}
			}
			atual = superior;
			superior = null;
		}

		// Inferior
		atual = agente;
		Objeto inferior = null;
		for (int i = 1; i <= alcance; i++) {
			if ((posX + i) <= configuracao.getTamanhoX() - 1) {
				inferior = tabuleiro[(posX + i)][posY];
				inferior.setConhecido(true);
				atual.setObjetoSul(inferior);
				inferior.setObjetoNorte(atual);
				
				if (inferior.getTipo() == TipoDeObjeto.PORTA) {
					agente.setPorta((Porta) inferior);
				}
				if (inferior.getTipo() == TipoDeObjeto.BAU) {
					agente.getBaus().add((Bau) inferior);
				}
			}
			atual = inferior;
			inferior = null;
		}

		// Esquerda
		atual = agente;
		Objeto esquerda = null;
		for (int i = 1; i <= alcance; i++) {
			if ((posY - i) >= 0) {
				esquerda = tabuleiro[(posX)][posY - i];
				esquerda.setConhecido(true);
				atual.setObjetoOeste(esquerda);
				esquerda.setObjetoLeste(atual);
				
				if (esquerda.getTipo() == TipoDeObjeto.PORTA) {
					agente.setPorta((Porta) esquerda);
				}
				if (esquerda.getTipo() == TipoDeObjeto.BAU) {
					agente.getBaus().add((Bau) esquerda);
				}
			}
			atual = esquerda;
			esquerda = null;
		}

		// Direita
		atual = agente;
		Objeto direita = null;
		for (int i = 1; i <= alcance; i++) {
			if ((posY + i) <= configuracao.getTamanhoY() - 1) {
				direita = tabuleiro[(posX)][posY + i];
				direita.setConhecido(true);
				atual.setObjetoLeste(direita);
				direita.setObjetoOeste(atual);
				
				if (direita.getTipo() == TipoDeObjeto.PORTA) {
					agente.setPorta((Porta) direita);
				}
				if (direita.getTipo() == TipoDeObjeto.BAU) {
					agente.getBaus().add((Bau) direita);
				}
			}
			atual = direita;
			direita = null;
		}
	}

	private void trocaObjeto(Objeto objetoAtual, Objeto objetoNovo) {
		objetoNovo.setObjetoNorte(objetoAtual.getObjetoNorte());
		objetoNovo.setObjetoSul(objetoAtual.getObjetoSul());
		objetoNovo.setObjetoLeste(objetoAtual.getObjetoLeste());
		objetoNovo.setObjetoOeste(objetoAtual.getObjetoOeste());

		if (objetoAtual.getObjetoNorte() != null) {
			objetoAtual.getObjetoNorte().setObjetoSul(objetoNovo);
		}
		if (objetoAtual.getObjetoSul() != null) {
			objetoAtual.getObjetoSul().setObjetoNorte(objetoNovo);
		}
		if (objetoAtual.getObjetoLeste() != null) {
			objetoAtual.getObjetoLeste().setObjetoOeste(objetoNovo);
		}
		if (objetoAtual.getObjetoOeste() != null) {
			objetoAtual.getObjetoOeste().setObjetoLeste(objetoNovo);
		}
		
		objetoAtual.setConhecido(true);
		objetoAtual.setVisitado(true);
		
		objetoNovo.setConhecido(true);
		objetoNovo.setVisitado(true);
		objetoNovo.setCoordenadas(objetoAtual.getCoordenadas());

		tabuleiro[objetoAtual.getPosicaoX()][objetoAtual.getPosicaoY()] = objetoNovo;
	}
	
	public void moverAgenteEPegarSacoDeMoedas(Direcao direcao) {
		Objeto objeto = null;

		if (direcao == Direcao.NORTE) {
			objeto = getObjeto(agente.getPosicaoX() - 1, agente.getPosicaoY());

		} else if (direcao == Direcao.SUL) {
			objeto = getObjeto(agente.getPosicaoX() + 1, agente.getPosicaoY());

		} else if (direcao == Direcao.LESTE) {
			objeto = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() + 1);

		} else if (direcao == Direcao.OESTE) {
			objeto = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() - 1);
		}

		if (objeto != null && objeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
			SacoDeMoedas sacoDeMoedas = (SacoDeMoedas) objeto;
			agente.getSacosDeMoedas().add(sacoDeMoedas);
			agente.adicionarPontos(sacoDeMoedas.getQuantidadeDeMoedas() * 10);
			
			trocaObjeto(objeto, new Livre(objeto.getPosicaoX(), objeto.getPosicaoY()));
			moverAgente(direcao);
		}
	}

	public void moverAgente(Direcao direcao) {
		Objeto objeto = null;

		if (direcao == Direcao.NORTE) {
			objeto = getObjeto(agente.getPosicaoX() - 1, agente.getPosicaoY());

		} else if (direcao == Direcao.SUL) {
			objeto = getObjeto(agente.getPosicaoX() + 1, agente.getPosicaoY());

		} else if (direcao == Direcao.LESTE) {
			objeto = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() + 1);

		} else if (direcao == Direcao.OESTE) {
			objeto = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() - 1);
		}

		if (objeto != null) {
			// && objeto.getTipo() != TipoDeObjeto.MURO && objeto.getTipo() !=
			// TipoDeObjeto.PORTA
			if (objeto.getTipo() == TipoDeObjeto.BURACO) {
				System.out.println("--------- Game Over. ---------");

			} else if (objeto.getTipo() == TipoDeObjeto.LIVRE) {
				Objeto substituto = null;
				if (!objetosParaRetornarAoTabuleiro.isEmpty()) {
					substituto = objetosParaRetornarAoTabuleiro.remove();
				} else {
					substituto = new Livre(agente.getPosicaoX(), agente.getPosicaoY());
				}
				
				trocaObjeto(agente, substituto);
				trocaObjeto(objeto, agente);
				mapearObjetosNoAlcanceDoAgente();
				iteracoes++;

			} else if (objeto.getTipo() == TipoDeObjeto.BAU 
					|| objeto.getTipo() == TipoDeObjeto.PORTA
					|| objeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
				
				Objeto substituto = null;
				if (!objetosParaRetornarAoTabuleiro.isEmpty()) {
					substituto = objetosParaRetornarAoTabuleiro.remove();
				} else {
					substituto = new Livre(agente.getPosicaoX(), agente.getPosicaoY());
				}
				
				objetosParaRetornarAoTabuleiro.add(objeto);
				
				trocaObjeto(agente, substituto);
				trocaObjeto(objeto, agente);
				mapearObjetosNoAlcanceDoAgente();
				iteracoes++;
			}
		}
	}

	public void moverAgenteComPulo(Direcao direcao) {	
		Objeto objeto = null;
		Objeto objetoDoObjeto = null;

		if (direcao == Direcao.NORTE) {
			objeto = getObjeto(agente.getPosicaoX() - 1, agente.getPosicaoY());
			objetoDoObjeto = objeto.getObjetoNorte();

		} else if (direcao == Direcao.SUL) {
			objeto = getObjeto(agente.getPosicaoX() + 1, agente.getPosicaoY());
			objetoDoObjeto = objeto.getObjetoSul();

		} else if (direcao == Direcao.LESTE) {
			objeto = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() + 1);
			objetoDoObjeto = objeto.getObjetoLeste();

		} else if (direcao == Direcao.OESTE) {
			objeto = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() - 1);
			objetoDoObjeto = objeto.getObjetoOeste();
		}

		if (objeto != null && objeto.getTipo() == TipoDeObjeto.BURACO) {
			if (objetoDoObjeto != null) {
				if (objetoDoObjeto.getTipo() == TipoDeObjeto.LIVRE) {
					
					Objeto substituto = null;
					if (!objetosParaRetornarAoTabuleiro.isEmpty()) {
						substituto = objetosParaRetornarAoTabuleiro.remove();
					} else {
						substituto = new Livre(agente.getPosicaoX(), agente.getPosicaoY());
					}
					
					trocaObjeto(agente, substituto);
					trocaObjeto(objetoDoObjeto, agente);
					
					objeto.setConhecido(true);
					objeto.setVisitado(true);
					agente.adicionarPontos(30);
					
					mapearObjetosNoAlcanceDoAgente();
					iteracoes++;
					
				} else if (objetoDoObjeto.getTipo() == TipoDeObjeto.BAU 
						|| objetoDoObjeto.getTipo() == TipoDeObjeto.PORTA
						|| objetoDoObjeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
					
					Objeto substituto = null;
					if (!objetosParaRetornarAoTabuleiro.isEmpty()) {
						substituto = objetosParaRetornarAoTabuleiro.remove();
					} else {
						substituto = new Livre(agente.getPosicaoX(), agente.getPosicaoY());
					}
										
					trocaObjeto(agente, substituto);
					objetosParaRetornarAoTabuleiro.add(objetoDoObjeto);
					trocaObjeto(objetoDoObjeto, agente);
					agente.adicionarPontos(30);
					
					mapearObjetosNoAlcanceDoAgente();
					iteracoes++;
				}
			}
		}
	}
	
	public void moverAgenteComPuloEPegarSacoDeMoedas(Direcao direcao) {	
		Objeto objeto = null;
		Objeto objetoDoObjeto = null;

		if (direcao == Direcao.NORTE) {
			objeto = getObjeto(agente.getPosicaoX() - 1, agente.getPosicaoY());
			objetoDoObjeto = objeto.getObjetoNorte();

		} else if (direcao == Direcao.SUL) {
			objeto = getObjeto(agente.getPosicaoX() + 1, agente.getPosicaoY());
			objetoDoObjeto = objeto.getObjetoSul();

		} else if (direcao == Direcao.LESTE) {
			objeto = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() + 1);
			objetoDoObjeto = objeto.getObjetoLeste();

		} else if (direcao == Direcao.OESTE) {
			objeto = getObjeto(agente.getPosicaoX(), agente.getPosicaoY() - 1);
			objetoDoObjeto = objeto.getObjetoOeste();
		}

		if (objeto != null && objeto.getTipo() == TipoDeObjeto.BURACO) {
			if (objetoDoObjeto != null && objetoDoObjeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
				SacoDeMoedas sacoDeMoedas = (SacoDeMoedas) objetoDoObjeto;
				agente.getSacosDeMoedas().add(sacoDeMoedas);
				agente.adicionarPontos(sacoDeMoedas.getQuantidadeDeMoedas() * 10);
				
				trocaObjeto(objetoDoObjeto, new Livre(objetoDoObjeto.getPosicaoX(), objetoDoObjeto.getPosicaoY()));
				moverAgenteComPulo(direcao);
				iteracoes++;
			}
		}
	}

	public void depositarSacosDeMoedas(Objeto objeto, SacoDeMoedas sacoDeMoedas) {
		if (objeto.getTipo() == TipoDeObjeto.BAU) {
			Bau bau = (Bau) objeto;
			bau.getSacosDeMoeda().add(sacoDeMoedas);
			agente.getSacosDeMoedas().remove(sacoDeMoedas);
		}
	}

}
