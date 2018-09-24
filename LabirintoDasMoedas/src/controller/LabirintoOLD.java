package controller;
import java.util.Random;
import java.util.Scanner;

import model.Objeto;
import model.Tabuleiro;

public class LabirintoOLD {

	private String[][] m;
	private int tamanhoX;
	private int tamanhoY;
	private int quantidadeDeBaus;
	private int quantidadeDeParedesInternas;
	private int tamanhoDasParedesInternas;
	private int quantidadeDeBuracos;
	private int quantidadeDeSacos;
	private int posicaoXAgente;
	private int posicaoYAgente;
	private String muro;
	private String muroTemporario;
	private String porta;
	private String bau;
	private String buraco;
	private String saco;
	private String agente;
	private String livre;

	public LabirintoOLD() throws Exception {
		tamanhoX = 10;
		tamanhoY = tamanhoX;
		quantidadeDeBaus = (tamanhoX + tamanhoY) / 5;
		quantidadeDeParedesInternas = (tamanhoX + tamanhoY) / 5;
		tamanhoDasParedesInternas = (tamanhoX + tamanhoY) / 4;
		quantidadeDeBuracos = (tamanhoX + tamanhoY) / 4;
		quantidadeDeSacos = quantidadeDeBaus * 4;
		
		muro = "M";
		muroTemporario = "T";
		porta = "P";
		bau = "B";
		buraco = "O";
		saco = "S";
		agente = "A";
		livre = "-";

		m = new String[tamanhoX][tamanhoY];
		criaLabirinto();
	}

	private void criaLabirinto() throws Exception {
		Random random = new Random();
		for (int i = 0; i < tamanhoX; i++) {
			for (int j = 0; j < tamanhoY; j++) {
				if (m[i][j] == null)
					m[i][j] = livre;// random.nextInt(100);
			}
		}
		criaParedao();
		criaPorta();
		criaBaus();
		criaParedesInternas();
		criaBuracos();
		criaSacos();
		criaAgente();
		
		Tabuleiro tabuleiro = new Tabuleiro();
		tabuleiro.imprimeTabuleiro();
	}

	private void criaParedao() {
		for (int i = 0; i < tamanhoY; i++) {
			m[i][tamanhoY - 1] = muro;
		}
	}

	private void criaPorta() {
		Random random = new Random();
		m[random.nextInt(tamanhoY)][tamanhoY - 1] = porta;
	}

	private void criaBaus() throws Exception {
		int bausCriados = 0;
		Random random = new Random();

		if (quantidadeDeBaus >= tamanhoY - 1) {
			throw new Exception("A matriz deve ter tamanho igual ou maior a 5");
		}

		while (bausCriados < quantidadeDeBaus) {
			int posicaoRandom = random.nextInt(tamanhoY);
			if (m[posicaoRandom][tamanhoY - 1].equals(muro) && m[posicaoRandom][tamanhoY - 2].equals(livre)) {
				m[posicaoRandom][tamanhoY - 2] = bau;
				bausCriados++;
			}
		}
	}

	private void criaParedesInternas() {
		Random random = new Random();
		boolean paredesInternasValidas = false;

		while (!paredesInternasValidas) {
			trocaParedesInternasTemporariasPorValor(livre);
			int paredesCriadas = 0;

			while (paredesCriadas < quantidadeDeParedesInternas) {
				int posicaoRandomLinha;
				int posicaoRandomColuna;
				int orientacao = random.nextInt(2); // Horizontal = 0; Vertical = 1;

				if (orientacao == 0) { // Horizontal
					posicaoRandomColuna = random.nextInt((tamanhoY / 2) - 1);
					posicaoRandomLinha = random.nextInt((tamanhoX));
				} else { // Vertical
					posicaoRandomColuna = random.nextInt((tamanhoY - 2));
					posicaoRandomLinha = random.nextInt((tamanhoX / 2) + 1);
				}

				if (paredeInternaValida(posicaoRandomLinha, posicaoRandomColuna, orientacao)) {
					for (int i = 0; i < tamanhoDasParedesInternas; i++) {
						m[posicaoRandomLinha][posicaoRandomColuna] = muroTemporario;
						if (orientacao == 0) { // Horizontal
							posicaoRandomColuna++;
						} else { // Vertical
							posicaoRandomLinha++;
						}
					}
					paredesCriadas++;
				}
			}
			paredesInternasValidas = paredesInternasValidas();
		}
	}

	private boolean paredeInternaValida(int posicaoRandomLinha, int posicaoRandomColuna, int orientacao) {
		int naoLivres = 0;
		for (int i = 0; i < tamanhoDasParedesInternas; i++) {
			if (!m[posicaoRandomLinha][posicaoRandomColuna].equals(livre)) {
				naoLivres++;
			}
			if (!vizinhoLivre(posicaoRandomLinha, posicaoRandomColuna)) {
				naoLivres++;
			}
			if (naoLivres > 1) {
				return false;
			}
			if (orientacao == 0) { // Horizontal
				posicaoRandomColuna++;
			} else { // Vertical
				posicaoRandomLinha++;
			}
		}
		return true;
	}

	private boolean vizinhoLivre(int posicaoRandomLinha, int posicaoRandomColuna) {
		if ((posicaoRandomLinha + 1) < tamanhoX) {
			if (!m[posicaoRandomLinha + 1][posicaoRandomColuna].equals(livre))
				return false;
		}
		if ((posicaoRandomLinha - 1) >= 0) {
			if (!m[posicaoRandomLinha - 1][posicaoRandomColuna].equals(livre))
				return false;
		}
		if ((posicaoRandomColuna + 1) < tamanhoY) {
			if (!m[posicaoRandomLinha][posicaoRandomColuna + 1].equals(livre))
				return false;
		}
		if ((posicaoRandomColuna - 1) >= 0) {
			if (!m[posicaoRandomLinha][posicaoRandomColuna - 1].equals(livre))
				return false;
		}

		if ((posicaoRandomLinha + 1) < tamanhoX) {
			if ((posicaoRandomColuna + 1) < tamanhoY) {
				if (!m[posicaoRandomLinha + 1][posicaoRandomColuna + 1].equals(livre))
					return false;
			}
			if ((posicaoRandomColuna - 1) >= 0) {
				if (!m[posicaoRandomLinha + 1][posicaoRandomColuna - 1].equals(livre))
					return false;
			}
		}

		if ((posicaoRandomLinha - 1) >= 0) {
			if ((posicaoRandomColuna + 1) < tamanhoY) {
				if (!m[posicaoRandomLinha - 1][posicaoRandomColuna + 1].equals(livre))
					return false;
			}
			if ((posicaoRandomColuna - 1) >= 0) {
				if (!m[posicaoRandomLinha - 1][posicaoRandomColuna - 1].equals(livre))
					return false;
			}
		}

		return true;
	}

	private boolean paredesInternasValidas() {
		for (int i = 0; i < tamanhoY - 1; i++) {
			int livres = 0;
			for (int j = 0; j < tamanhoX; j++) {
				if (m[j][i].equals(livre))
					livres++;
			}
			if (livres == 0)
				return false;
		}

		trocaParedesInternasTemporariasPorValor(muro);
		return true;
	}

	private void trocaParedesInternasTemporariasPorValor(String valor) {
		for (int i = 0; i < tamanhoX; i++) {
			for (int j = 0; j < tamanhoY; j++) {
				if (m[i][j].equals(muroTemporario)) {
					m[i][j] = valor;
				}
			}
		}
	}

	private void criaBuracos() {
		int buracosCriados = 0;
		Random random = new Random();

		while (buracosCriados < quantidadeDeBuracos) {
			int posicaoRandomX = random.nextInt(tamanhoX);
			int posicaoRandomY = random.nextInt(tamanhoY - 1);

			if (m[posicaoRandomX][posicaoRandomY].equals(livre) && !existeBuracoVizinho(posicaoRandomX, posicaoRandomY)
					&& !estaEmUmCanto(posicaoRandomX, posicaoRandomY)) {
				m[posicaoRandomX][posicaoRandomY] = buraco;
				buracosCriados++;
			}
		}
	}

	private boolean estaEmUmCanto(int posicaoRandomX, int posicaoRandomY) {
		if (posicaoRandomX == 0 && posicaoRandomY == 0 
				|| posicaoRandomX == tamanhoX - 1 && posicaoRandomY == 0
				|| posicaoRandomX == tamanhoX - 1 && posicaoRandomY == tamanhoY - 2
				|| posicaoRandomX == 0 && posicaoRandomY == tamanhoY - 2) {
			return true;
		}
		
		if (posicaoRandomX >= 0 && posicaoRandomX < tamanhoX - 1) {
			if (m[posicaoRandomX+1][posicaoRandomY].equals(muro)) {
				if (posicaoRandomY > 0) {
					if (m[posicaoRandomX][posicaoRandomY-1].equals(muro)) {
						return true;
					}
				}
				if (posicaoRandomY < tamanhoY - 1) {
					if (m[posicaoRandomX][posicaoRandomY+1].equals(muro)) {
						return true;
					}
				}
			}
		}
		
		if (posicaoRandomX <= tamanhoX - 1 && posicaoRandomX > 0) {
			if (m[posicaoRandomX-1][posicaoRandomY].equals(muro)) {
				if (posicaoRandomY > 0) {
					if (m[posicaoRandomX][posicaoRandomY-1].equals(muro)) {
						return true;
					}
				}
				if (posicaoRandomY < tamanhoY - 1) {
					if (m[posicaoRandomX][posicaoRandomY+1].equals(muro)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

	private boolean existeBuracoVizinho(int posicaoX, int posicaoY) {
		if (posicaoX == 0) {
			if (m[posicaoX + 1][posicaoY].equals(buraco)) {
				return true;
			}
		} else if (posicaoX == tamanhoX - 1) {
			if (m[posicaoX - 2][posicaoY].equals(buraco)) {
				return true;
			}
		} else {
			if (m[posicaoX + 1][posicaoY].equals(buraco)) {
				return true;
			}
			if (m[posicaoX - 1][posicaoY].equals(buraco)) {
				return true;
			}
		}

		if (posicaoY != 0) {
			if (m[posicaoX][posicaoY - 1].equals(buraco)) {
				return true;
			}
		}
		if (m[posicaoX][posicaoY + 1].equals(buraco)) {
			return true;
		}

		return false;
	}
	
	private void criaSacos() {
		int sacosCriados = 0;
		Random random = new Random();

		while (sacosCriados < quantidadeDeSacos) {
			int posicaoRandomX = random.nextInt(tamanhoX);
			int posicaoRandomY = random.nextInt(tamanhoY - 1);

			if (m[posicaoRandomX][posicaoRandomY].equals(livre)) {
				m[posicaoRandomX][posicaoRandomY] = saco;
				sacosCriados++;
			}
		}
	}

	private void criaAgente() {
		Random random = new Random();
		boolean agenteCriado = false;

		while (!agenteCriado) {
			int posicaoRandomX = random.nextInt(tamanhoX);
			int posicaoRandomY = random.nextInt(tamanhoY - 1);

			if (m[posicaoRandomX][posicaoRandomY].equals(livre)) {
				m[posicaoRandomX][posicaoRandomY] = agente;
				posicaoXAgente = posicaoRandomX;
				posicaoYAgente = posicaoRandomY;
				agenteCriado = true;
			}
		}
	}

	private int getPosicaoXAgente() {
		return posicaoXAgente;
	}

	private int getPosicaoYAgente() {
		return posicaoYAgente;
	}

	public void setPosicaoXAgente(int posicaoXAgente) {
		this.posicaoXAgente = posicaoXAgente;
	}

	public void setPosicaoYAgente(int posicaoYAgente) {
		this.posicaoYAgente = posicaoYAgente;
	}

	public void imprimeLabirinto() {
		StringBuilder sb;
		for (int i = 0; i < tamanhoX; i++) {
			sb = new StringBuilder();
			for (int j = 0; j < tamanhoY; j++) {
				sb.append(" ");
				sb.append(insereEspacos(m[i][j], 2));
				// System.out.println("m["+i+"]["+j+"]: " + m[i][j]);
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

	public boolean moveAgente(String movimento) {
		if (movimento.equals("a")) {

		} else if (movimento.equals("s")) {

		} else if (movimento.equals("w")) {
			return moveAgenteParaCima();
		} else if (movimento.equals("d")) {

		}
		return false;
	}

	private boolean moveAgenteParaCima() {
		if (m[getPosicaoXAgente() - 1][getPosicaoYAgente()].equals(livre)) {
			m[getPosicaoXAgente()][getPosicaoYAgente()] = livre;
			m[getPosicaoXAgente() - 1][getPosicaoYAgente()] = agente;
			setPosicaoXAgente(getPosicaoXAgente() - 1);
			return true;
		}
		return false;
	}

	private boolean moveAgenteParaBaixo() {
		if (m[getPosicaoXAgente() + 1][getPosicaoYAgente()].equals(livre)) {
			m[getPosicaoXAgente()][getPosicaoYAgente()] = livre;
			m[getPosicaoXAgente() + 1][getPosicaoYAgente()] = agente;
			setPosicaoXAgente(getPosicaoXAgente() + 1);
			return true;
		}
		return false;
	}

	private boolean moveAgenteParaEsquerda() {
		if (m[getPosicaoXAgente()][getPosicaoYAgente() - 1].equals(livre)) {
			m[getPosicaoXAgente()][getPosicaoYAgente()] = livre;
			m[getPosicaoXAgente()][getPosicaoYAgente() - 1] = agente;
			setPosicaoYAgente(getPosicaoYAgente() - 1);
			return true;
		}
		return false;
	}

	private boolean moveAgenteParaDireita() {
		if (m[getPosicaoXAgente()][getPosicaoYAgente() + 1].equals(livre)) {
			m[getPosicaoXAgente()][getPosicaoYAgente()] = livre;
			m[getPosicaoXAgente()][getPosicaoYAgente() + 1] = agente;
			setPosicaoYAgente(getPosicaoYAgente() + 1);
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		LabirintoOLD labirinto = new LabirintoOLD();

		String comando = "";

		System.out.println("Bem vindo ao jogo!");
		System.out.println();

		// while (true) {
		labirinto.imprimeLabirinto();

		// comando = sc.nextLine();
		// labirinto.moveAgente(comando);
		// }
	}

}
