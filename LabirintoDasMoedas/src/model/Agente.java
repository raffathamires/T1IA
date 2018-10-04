package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class Agente extends Objeto {

	private List<SacoDeMoedas> sacosDeMoedas;
	private int pontuacao;
	private Tabuleiro tabuleiro;
	private Porta porta;
	private Set<Bau> baus;
	private EstadoDoAgente estadoAtual;
	
	private Bau distribuicaoBau0 = new Bau(0, 0);
	private Bau distribuicaoBau1 = new Bau(0, 0);
	private Bau distribuicaoBau2 = new Bau(0, 0);
	private Bau distribuicaoBau3 = new Bau(0, 0);

	public Agente(int posicaoX, int posicaoY, Tabuleiro tabuleiro) {
		super(posicaoX, posicaoY, TipoDeObjeto.AGENTE);
		this.tabuleiro = tabuleiro;
		this.sacosDeMoedas = new ArrayList<SacoDeMoedas>();
		this.baus = new HashSet<Bau>();
		this.pontuacao = 0;
		this.visitado = true;
		this.porta = null;
		this.estadoAtual = EstadoDoAgente.PROCURANDO_PORTA;
	}

	public List<SacoDeMoedas> getSacosDeMoedas() {
		return sacosDeMoedas;
	}

	public void setSacosDeMoedas(List<SacoDeMoedas> sacosDeMoedas) {
		this.sacosDeMoedas = sacosDeMoedas;
	}

	public Set<Bau> getBaus() {
		return baus;
	}

	public void setBaus(Set<Bau> baus) {
		this.baus = baus;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	public void setTabuleiro(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	public Porta getPorta() {
		return porta;
	}

	public void setPorta(Porta porta) {
		this.porta = porta;
	}

	public void adicionarPontos(int pontos) {
		setPontuacao(getPontuacao() + pontos);
	}

	public EstadoDoAgente getEstadoAtual() {
		return estadoAtual;
	}

	public void setEstadoAtual(EstadoDoAgente estadoAtual) {
		this.estadoAtual = estadoAtual;
	}

	public void mover(Direcao direcao) {
		Objeto objeto = null;
		Objeto objetoDoObjeto = null;

		if (direcao == Direcao.NORTE && getObjetoNorte() != null) {
			objeto = getObjetoNorte();
			objetoDoObjeto = getObjetoNorte().getObjetoNorte();

		} else if (direcao == Direcao.SUL && getObjetoSul() != null) {
			objeto = getObjetoSul();
			objetoDoObjeto = getObjetoSul().getObjetoSul();

		} else if (direcao == Direcao.LESTE && getObjetoLeste() != null) {
			objeto = getObjetoLeste();
			objetoDoObjeto = getObjetoLeste().getObjetoLeste();

		} else if (direcao == Direcao.OESTE && getObjetoOeste() != null) {
			objeto = getObjetoOeste();
			objetoDoObjeto = getObjetoOeste().getObjetoOeste();

		} else {
			return;
		}

		if (objeto.getTipo() == TipoDeObjeto.BURACO) {
			if (objetoDoObjeto != null && objetoDoObjeto.getTipo() != TipoDeObjeto.MURO
					&& objetoDoObjeto.getTipo() != TipoDeObjeto.BURACO) {
				if (objetoDoObjeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
					getTabuleiro().moverAgenteComPuloEPegarSacoDeMoedas(direcao);
				} else {
					getTabuleiro().moverAgenteComPulo(direcao);
				}
			}

		} else if (objeto.getTipo() == TipoDeObjeto.SACO_DE_MOEDAS) {
			getTabuleiro().moverAgenteEPegarSacoDeMoedas(direcao);

		} else {
			getTabuleiro().moverAgente(direcao);
		}

		tabuleiro.imprimeTabuleiro();
		System.out.println("------------------------------");
		tabuleiro.imprimeTabuleiroVisivelPeloAgente();
		System.out.println("------------------------------");

		try {
			Thread.sleep(getTabuleiro().getConfiguracao().getSleepTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private Direcao getDirecaoTipoDeObjetoAVista(TipoDeObjeto tipoDeObjeto) {
		for (Objeto vizinho : getVizinhos()) {
			if (vizinho.getTipo() == tipoDeObjeto) {
				return getDirecaoVizinho(vizinho);
			}
		}

		for (Objeto vizinho : getVizinhos()) {
			Direcao direcao = getDirecaoVizinho(vizinho);
			Objeto vizinhoDoVizinho = vizinho.getVizinhoPelaDirecao(direcao);

			if (vizinhoDoVizinho != null && vizinhoDoVizinho.getTipo() == tipoDeObjeto) {
				if (vizinho.getTipo() != TipoDeObjeto.MURO) {
					return direcao;

				} else {
					List<Coordenadas> coordenadas = aStarSearch(getCoordenadas(), vizinhoDoVizinho.getCoordenadas());
					if (coordenadas != null && coordenadas.size() > 1) {
						return getDirecaoVizinho(
								tabuleiro.getObjetoPelasCoordenadas(coordenadas.get(coordenadas.size() - 2)));
					}
				}
			}
		}

		return null;
	}

	private Bau getBauComMenosMoedas() {
		Bau bauComMenosMoedas = null;

		for (Bau bau : baus) {
			if (bauComMenosMoedas == null) {
				bauComMenosMoedas = bau;
			} else {
				if (bau.getQuantidadeTotalDeMoedas() < bauComMenosMoedas.getQuantidadeTotalDeMoedas()) {
					bauComMenosMoedas = bau;
				}
			}
		}

		return bauComMenosMoedas;
	}

	private boolean distribuicaoDeMoedasValida() {
		return geneticDistribution();
	}

	private List<Coordenadas> aStarSearch(Coordenadas agente, Coordenadas objetivo) {

		Objeto startNode = tabuleiro.getObjetoPelasCoordenadas(agente);
		Objeto endNode = tabuleiro.getObjetoPelasCoordenadas(objetivo);

		// configuracoes do A*
		HashMap<Objeto, Objeto> parentMap = new HashMap<Objeto, Objeto>();
		HashSet<Objeto> visited = new HashSet<Objeto>();
		Map<Objeto, Double> distances = tabuleiro.initializeAllToInfinity();

		Queue<Objeto> priorityQueue = initQueue();

		// empilha o StartNode, com distancia 0
		startNode.setDistanciaDoAgente(new Double(0));
		distances.put(startNode, new Double(0));
		priorityQueue.add(startNode);
		Objeto current = null;

		while (!priorityQueue.isEmpty()) {
			current = priorityQueue.remove();

			if (!visited.contains(current)) {
				visited.add(current);
				// se ultimo elemento na priorityQueue encontrou, retorna o caminho
				if (current.equals(endNode)) {
					return reconstructPath(parentMap, startNode, endNode, 0);
				}

				Set<Objeto> vizinhos = current.getVizinhosLivresValidandoNaoVisitado(false);
				for (Objeto vizinho : vizinhos) {
					if (vizinho != null && !visited.contains(vizinho)) {

						// calcula distancia estimada até o objetivo usando a heuristica
						double predictedDistance = vizinho.calcularHeuristica(endNode);

						// 1. calcula distancia até o vizinho. 2. calcula distancia estimada do agente
						double vizinhoDistance = current.calcularDistancia(vizinho);
						double totalDistance = current.getDistanciaDoAgente() + vizinhoDistance + predictedDistance;

						// check if distance smaller
						if (totalDistance < distances.get(vizinho)) {
							// update n's distance
							distances.put(vizinho, totalDistance);
							// usado pela PriorityQueue
							vizinho.setDistanciaDoAgente(totalDistance);
							vizinho.setDistanciaEstimada(predictedDistance);
							// set parent
							parentMap.put(vizinho, current);
							// enqueue
							priorityQueue.add(vizinho);
						}
					}
				}
			}
		}
		return null;
	}

	private List<Coordenadas> reconstructPath(HashMap<Objeto, Objeto> parentMap, Objeto start, Objeto goal, int x) {
		List<Coordenadas> path = new ArrayList<>();
		Objeto current = goal;

		while (current != null) {
			path.add(current.getCoordenadas());
			current = parentMap.get(current);
		}
		return path;
	}

	public boolean direcaoValida(Direcao direcao) {
		if (direcao == null || getVizinhoPelaDirecao(direcao) == null
				|| getVizinhoPelaDirecao(direcao).getTipo() == TipoDeObjeto.MURO
				|| (getVizinhoPelaDirecao(direcao).getTipo() == TipoDeObjeto.BURACO
						&& (getVizinhoPelaDirecao(direcao).getVizinhoPelaDirecao(direcao) == null
								|| getVizinhoPelaDirecao(direcao).getVizinhoPelaDirecao(direcao)
										.getTipo() == TipoDeObjeto.MURO
								|| getVizinhoPelaDirecao(direcao).getVizinhoPelaDirecao(direcao)
										.getTipo() == TipoDeObjeto.BURACO))) {
			return false;
		}
		return true;
	}

	public boolean geneticDistribution() {
		int qtdBaus = getTabuleiro().getConfiguracao().getQuantidadeDeBaus();

		List<SacoDeMoedas> carga = inicializarCarga();
		int limite = carga.size()-1;
		
		int[][] populacao = new int[qtdBaus + 1][limite + 1];
		int[][] intermediaria = new int[qtdBaus + 1][limite + 1];
		
		popular(populacao, limite);

		for (int i = 0; i <= limite; i++) {
			System.out.println("Geracao " + i);
			aptidar(populacao, carga, limite);
			printPopulacao(populacao, limite);
			elitizar(populacao, intermediaria, limite);

			gerar(populacao, intermediaria, limite);
		}
		
		for (int i = 0; i < qtdBaus + 1; i++) {
			for (int j = 0; j < limite + 1; j++) {
				if (populacao[i][j] == 4) {
					System.out.println("Achou um 4");
					printPopulacao(populacao, limite);
					return true;
				}
			}
		}
		return false;
	}

	private List<SacoDeMoedas> inicializarCarga() {
		List<SacoDeMoedas> carga = new ArrayList<SacoDeMoedas>();

		for (SacoDeMoedas sacoDeMoedas : getSacosDeMoedas()) {
			carga.add(sacoDeMoedas);
		}
		
		return carga;
	}

	private void clonar(int[][] destino, int[][] origem, int limite) {
		int qtdBaus = getTabuleiro().getConfiguracao().getQuantidadeDeBaus();
		int qtdSacosDeMoedas = getTabuleiro().getConfiguracao().getQuantidadeDeSacosDeMoedas();

		for (int i = 0; i <= qtdBaus; i++) {
			for (int j = 0; j <= limite; j++) {
				destino[i][j] = origem[i][j];
			}
		}
	}

	private void clonar(int[] destino, int[] origem, int limite) {
		int qtdSacosDeMoedas = getTabuleiro().getConfiguracao().getQuantidadeDeSacosDeMoedas();

		for (int j = 0; j <= limite; j++) {
			destino[j] = origem[j];
		}
	}

	private void gerar(int[][] populacao, int[][] intermediaria, int limite) {
		int qtdBaus = getTabuleiro().getConfiguracao().getQuantidadeDeBaus();
		int linha = 0;

		for (int i = 0; i < 2; i++) {
			int pai = torneio(populacao, limite);
			int mae = torneio(populacao, limite);
			linha++;
			for (int j = 0; j <= limite / 2; j++) {
				intermediaria[linha][j] = populacao[pai][j];
				intermediaria[linha + 1][j] = populacao[mae][j];
			}
			for (int j = qtdBaus + 1; j <= limite; j++) {
				intermediaria[linha][j] = populacao[mae][j];
				intermediaria[linha + 1][j] = populacao[pai][j];
			}
			linha++;
		}
		
		clonar(populacao, intermediaria, limite);
	}

	private int torneio(int[][] populacao, int limite) {
		int qtdBaus = getTabuleiro().getConfiguracao().getQuantidadeDeBaus();
		Random r = new Random();

		int primeiro = r.nextInt(qtdBaus + 1);
		int segundo = r.nextInt(qtdBaus + 1);

		return (populacao[primeiro][limite] < populacao[segundo][limite]) ? primeiro : segundo;
	}

	private void elitizar(int[][] populacao, int[][] intermediaria, int limite) {
		int qtdBaus = getTabuleiro().getConfiguracao().getQuantidadeDeBaus();
		
		int indexMenor = 0;

		for (int i = 0; i <= qtdBaus; i++) {
			if (populacao[i][limite] < populacao[indexMenor][limite]) {
				indexMenor = i;
			}
		}
		
		clonar(intermediaria[0], populacao[indexMenor], limite);
	}

	private void aptidar(int[][] populacao, List<SacoDeMoedas> carga, int limite) {
		int qtdBaus = getTabuleiro().getConfiguracao().getQuantidadeDeBaus();
		distribuicaoBau0.setSacosDeMoeda(new ArrayList<SacoDeMoedas>());
		distribuicaoBau1.setSacosDeMoeda(new ArrayList<SacoDeMoedas>());
		distribuicaoBau2.setSacosDeMoeda(new ArrayList<SacoDeMoedas>());
		distribuicaoBau3.setSacosDeMoeda(new ArrayList<SacoDeMoedas>());

		for (int i = 0; i <= qtdBaus; i++) {
			populacao[i][limite] = 0;
			int cont = 0;
			
			
			for (int j = 0; j <= limite; j++) {
				if (populacao[i][j] == 0) {
					distribuicaoBau0.getSacosDeMoeda().add(carga.get(j));
				}
				if (populacao[i][j] == 1) {
					distribuicaoBau1.getSacosDeMoeda().add(carga.get(j));
				}
				if (populacao[i][j] == 2) {
					distribuicaoBau2.getSacosDeMoeda().add(carga.get(j));
				}
				if (populacao[i][j] == 3) {
					distribuicaoBau3.getSacosDeMoeda().add(carga.get(j));
				}
			}
			
			int bau0 = distribuicaoBau0.getQuantidadeTotalDeMoedas();
			int bau1 = distribuicaoBau1.getQuantidadeTotalDeMoedas();
			int bau2 = distribuicaoBau2.getQuantidadeTotalDeMoedas();
			int bau3 = distribuicaoBau3.getQuantidadeTotalDeMoedas();
			
			int total = bau0 + bau1 + bau2 + bau3;
			
			int minMoedas = getTabuleiro().getConfiguracao().getQuantidadeMinimaDeMoedasNecessariasEmCadaBau();

			if (total / 4 == bau0 && bau0 >= minMoedas) {
				cont++;
			}
			if (total / 4 == bau1 && bau1 >= minMoedas) {
				cont++;
			}
			if (total / 4 == bau2 && bau2 >= minMoedas) {
				cont++;
			}
			if (total / 4 == bau3 && bau3 >= minMoedas) {
				cont++;
			}

			populacao[i][limite] = cont;
		}
	}

	private void popular(int[][] populacao, int limite) {
		int qtdBaus = getTabuleiro().getConfiguracao().getQuantidadeDeBaus();

		Random r = new Random();
		for (int i = 0; i <= qtdBaus; i++)
			for (int j = 0; j <= limite; j++) {
				populacao[i][j] = r.nextInt(qtdBaus);
			}
	}

	private void printPopulacao(int[][] populacao, int limite) {
		int qtdBaus = getTabuleiro().getConfiguracao().getQuantidadeDeBaus();

		for (int i = 0; i <= qtdBaus; i++) {
			for (int j = 0; j <= limite; j++) {
				System.out.print(populacao[i][j] + " ");
			}
			System.out.println("");
		}
	}

	private PriorityQueue<Objeto> initQueue() {
		return new PriorityQueue<>(10, new Comparator<Objeto>() {
			public int compare(Objeto x, Objeto y) {
				if (x.getDistanciaDoAgente() < y.getDistanciaDoAgente()) {
					return -1;
				}
				if (x.getDistanciaDoAgente() > y.getDistanciaDoAgente()) {
					return 1;
				}
				return 0;
			};
		});
	}

	public Direcao estrategiaMoverAutonomo(Direcao direcao) {
		Random random = new Random();
		boolean achouAlgoImportante = false;

		if (getDirecaoTipoDeObjetoAVista(TipoDeObjeto.BAU) != null) {
			
			Objeto objeto = getVizinhoPelaDirecao(direcao);
			if (objeto != null) {
				if (objeto.getTipo() == TipoDeObjeto.BAU && !getBaus().contains(objeto)) {
					direcao = getDirecaoTipoDeObjetoAVista(TipoDeObjeto.BAU);
					achouAlgoImportante = true;
				} 
				
				objeto = objeto.getVizinhoPelaDirecao(direcao);
				if (objeto != null && objeto.getTipo() == TipoDeObjeto.BAU && !getBaus().contains(objeto)) {
					direcao = getDirecaoTipoDeObjetoAVista(TipoDeObjeto.BAU);
					achouAlgoImportante = true;
				}
			}
		}

		if (getDirecaoTipoDeObjetoAVista(TipoDeObjeto.SACO_DE_MOEDAS) != null) {
			direcao = getDirecaoTipoDeObjetoAVista(TipoDeObjeto.SACO_DE_MOEDAS);
			achouAlgoImportante = true;
		}

		if (!direcaoValida(direcao) || (!achouAlgoImportante && random.nextInt(5) == 3)) {
			Set<Objeto> vizinhosLivres = getVizinhosLivres();

			if (vizinhosLivres.isEmpty()) {
				System.err.println("Agente encurralado!");
				setEstadoAtual(EstadoDoAgente.GAME_OVER);
			}

			Objeto vizinho = (Objeto) vizinhosLivres.toArray()[random.nextInt(vizinhosLivres.size())];
			direcao = getDirecaoVizinho(vizinho);

		}
		mover(direcao);
		return direcao;
	}

	public void ativarModoAutonomo() {
		int tentativasRestantes = getTabuleiro().getConfiguracao().getMaximoDeTentativas();
		
		while (estadoAtual != EstadoDoAgente.FORA_DO_LABIRINTO && estadoAtual != EstadoDoAgente.GAME_OVER) {

			Direcao direcao = null;

			while (estadoAtual == EstadoDoAgente.PROCURANDO_PORTA) {
				if (porta != null) {
					setEstadoAtual(EstadoDoAgente.PROCURANDO_BAUS);
					break;
				}
				direcao = estrategiaMoverAutonomo(direcao);
			}

			while (estadoAtual == EstadoDoAgente.PROCURANDO_BAUS) {
				if (baus.size() == tabuleiro.getConfiguracao().getQuantidadeDeBaus()) {
					setEstadoAtual(EstadoDoAgente.PROCURANDO_SACOS_DE_MOEDA);
					break;
				}

				direcao = estrategiaMoverAutonomo(direcao);
			}
			
			int qtdSacosDeMoedas = getSacosDeMoedas().size();
			while (estadoAtual == EstadoDoAgente.PROCURANDO_SACOS_DE_MOEDA) {
				if (getSacosDeMoedas().size() == getTabuleiro().getConfiguracao().getQuantidadeDeSacosDeMoedas()) {
					qtdSacosDeMoedas = -1;
					tentativasRestantes--;
					
					if (tentativasRestantes <= 0) {
						setEstadoAtual(EstadoDoAgente.GAME_OVER);
						break;
					}
				}
				
				if (qtdSacosDeMoedas != getSacosDeMoedas().size() && distribuicaoDeMoedasValida()) {
					setEstadoAtual(EstadoDoAgente.DISTRIBUINDO_MOEDAS);
					break;
				}
				
				qtdSacosDeMoedas = getSacosDeMoedas().size();
				
				direcao = estrategiaMoverAutonomo(direcao);
			}

			while (estadoAtual == EstadoDoAgente.DISTRIBUINDO_MOEDAS) {
				if (distribuicaoDeMoedasValida()) {
					setEstadoAtual(EstadoDoAgente.SAINDO_DO_LABIRINTO);
					break;
				}

				Bau bau = getBauComMenosMoedas();

				if (bau != null) {
					List<Coordenadas> coordenadas = aStarSearch(getCoordenadas(), bau.getCoordenadas());

					if (coordenadas != null) {
						if (coordenadas.size() == 1 || coordenadas.size() == 2) {
							// chegou no bau
							while (sacosDeMoedas.size() > 0 && (bau.getQuantidadeTotalDeMoedas() < tabuleiro
									.getConfiguracao().getQuantidadeMinimaDeMoedasNecessariasEmCadaBau()
									|| bau == getBauComMenosMoedas())) {
								getTabuleiro().depositarSacosDeMoedas(bau, getSacosDeMoedas().iterator().next());
							}
						} else {
							direcao = getDirecaoVizinho(
									tabuleiro.getObjetoPelasCoordenadas(coordenadas.get(coordenadas.size() - 2)));
						}
					} else {
						System.err.println("Erro na execucao do algoritmo A* para localizar o bau com menos moedas!");
						setEstadoAtual(EstadoDoAgente.GAME_OVER);
						break;
					}
				} else {
					System.err.println("Erro na localização do bau com menos moedas!");
					setEstadoAtual(EstadoDoAgente.GAME_OVER);
					break;
				}
				mover(direcao);
			}

			while (estadoAtual == EstadoDoAgente.SAINDO_DO_LABIRINTO) {
				// USAR A*, PARA IR ATE A PORTA E SAIR
				if (porta == null) {
					estadoAtual = EstadoDoAgente.PROCURANDO_PORTA;
				}
				List<Coordenadas> coordenadas = aStarSearch(getCoordenadas(), porta.getCoordenadas());

				if (coordenadas != null) {
					if (coordenadas.size() == 1) {
						// chegou na porta
						estadoAtual = EstadoDoAgente.FORA_DO_LABIRINTO;
						break;
					} else {
						direcao = getDirecaoVizinho(
								tabuleiro.getObjetoPelasCoordenadas(coordenadas.get(coordenadas.size() - 2)));
					}
					mover(direcao);
				} else {
					System.err.println("Erro na execucao do algoritmo A* para localizar a porta e sair!");
					setEstadoAtual(EstadoDoAgente.GAME_OVER);
					break;
				}

			}

		}

		if (estadoAtual == EstadoDoAgente.FORA_DO_LABIRINTO) {
			System.out.println("-*--*--*--*--*--*--*--*--*--*--*-");
			System.out.println("Labirinto concluído com sucesso!");
			System.out.println("-*--*--*--*--*--*--*--*--*--*--*-");

		} else if (estadoAtual == EstadoDoAgente.GAME_OVER) {
			System.out.println("-x--x--x--x--x--x--x--x--x--x--x-");
			System.out.println("GAME OVER");
			System.out.println("-x--x--x--x--x--x--x--x--x--x--x-");

		}
	}

	@Override
	public String toString() {
		return "A";
	}
}
