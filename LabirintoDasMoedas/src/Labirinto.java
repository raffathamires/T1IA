import java.util.Random;
import java.util.Scanner;

/*
 * M = Muro
 * P = Porta
 * B = Baú
 * O = Buraco
 * A = Agente
 */
public class Labirinto {

	private String[][] m;
	private int tamanhoX;
	private int tamanhoY;
	private int quantidadeDeBaus;
	private int quantidadeDeParedesInternas;
	private int tamanhoDasParedesInternas;
	private int quantidadeDeBuracos;
	private int posicaoXAgente;
	private int posicaoYAgente;

	public Labirinto() throws Exception {
		tamanhoX = 10;
		tamanhoY = tamanhoX;
		quantidadeDeBaus = (tamanhoX+tamanhoY)/5;
		quantidadeDeParedesInternas = (tamanhoX+tamanhoY)/5;
		tamanhoDasParedesInternas = (tamanhoX+tamanhoY)/4;
		quantidadeDeBuracos = (tamanhoX+tamanhoY)/4;

		m = new String[tamanhoX][tamanhoY];
		populaLabirinto();
	}

	private void populaLabirinto() throws Exception {
		Random random = new Random();
		for (int i = 0; i < tamanhoX; i++) {
			for (int j = 0; j < tamanhoY; j++) {
				if (m[i][j] == null)
					m[i][j] = "-";// random.nextInt(100);
			}
		}
		criaParedao();
		criaPorta();
		criaBaus();
		criaParedesInternas();
		criaBuracos();
		criaAgente();
	}

	private void criaParedao() {
		for (int i = 0; i < tamanhoY; i++) {
			m[i][tamanhoY - 1] = "M";
		}
	}

	private void criaPorta() {
		Random random = new Random();
		m[random.nextInt(tamanhoY)][tamanhoY - 1] = "P";
	}

	private void criaBaus() throws Exception {
		int bausCriados = 0;
		Random random = new Random();

		if (quantidadeDeBaus >= tamanhoY - 1) {
			throw new Exception("A matriz deve ter tamanho igual ou maior a 5");
		}

		while (bausCriados < quantidadeDeBaus) {
			int posicaoRandom = random.nextInt(tamanhoY);
			if (m[posicaoRandom][tamanhoY - 1].equals("M") && m[posicaoRandom][tamanhoY - 2].equals("-")) {
				m[posicaoRandom][tamanhoY - 2] = "B";
				bausCriados++;
			}
		}
	}

	private void criaParedesInternas() {
		Random random = new Random();
		boolean paredesInternasValidas = false;

		while (!paredesInternasValidas) {
			int paredesCriadas = 0;

			while (paredesCriadas < quantidadeDeParedesInternas) {
				int posicaoRandomLinha;
				int posicaoRandomColuna;
				int orientacao = random.nextInt(2);

				if (orientacao == 0) { // Horizontal
					posicaoRandomColuna = random.nextInt((tamanhoY / 2) - 1);
					posicaoRandomLinha = random.nextInt((tamanhoX));
				} else { // Vertical
					posicaoRandomColuna = random.nextInt((tamanhoY - 2));
					posicaoRandomLinha = random.nextInt((tamanhoX / 2) + 1);
				}

				if (orientacao == 0) {
					for (int i = 0; i < tamanhoDasParedesInternas; i++) {
						m[posicaoRandomLinha][posicaoRandomColuna] = "T";
						posicaoRandomColuna++;
					}
					paredesCriadas++;
				} else {
					for (int i = 0; i < tamanhoDasParedesInternas; i++) {
						m[posicaoRandomLinha][posicaoRandomColuna] = "T";
						posicaoRandomLinha++;
					}
					paredesCriadas++;
				}
			}
			paredesInternasValidas = paredesInternasValidas();
		}
	}

	private boolean paredesInternasValidas() {
		/*
		 * this.imprimeLabirinto();
		 * System.out.println("------------------------------"); for (int i = 0; i <
		 * tamanhoX-1; i++) { for (int j = 0; j < tamanhoY-1; j++) {
		 * if(m[i+1][j].equals("T") && m[i][j+1].equals("T")) {
		 * trocaParedesInternasTemporariasPorValor("-"); return false; } } }
		 * 
		 * for (int i = 0; i < tamanhoX; i++) { int celulasLiberadas = 0; for (int j =
		 * 0; j < tamanhoY; j++) { if(!m[i][j].equals("T")) { celulasLiberadas++; } }
		 * if(celulasLiberadas==0) return false; }
		 */
		trocaParedesInternasTemporariasPorValor("M");
		return true;
	}

	private void trocaParedesInternasTemporariasPorValor(String valor) {
		for (int i = 0; i < tamanhoX; i++) {
			for (int j = 0; j < tamanhoY; j++) {
				if (m[i][j].equals("T")) {
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

			if (m[posicaoRandomX][posicaoRandomY].equals("-") && !existeBuracoVizinho(posicaoRandomX, posicaoRandomY)) {
				m[posicaoRandomX][posicaoRandomY] = "O";
				buracosCriados++;
			}
		}
	}

	private boolean existeBuracoVizinho(int posicaoX, int posicaoY) {
		if (posicaoX == 0) {
			if (m[posicaoX + 1][posicaoY].equals("O"))
				return true;

		} else if (posicaoX == tamanhoX - 1) {
			if (m[posicaoX - 1][posicaoY].equals("O"))
				return true;

		} else {
			if (m[posicaoX + 1][posicaoY].equals("O"))
				return true;
			if (m[posicaoX - 1][posicaoY].equals("O"))
				return true;
		}

		if (posicaoY != 0) {
			if (m[posicaoX][posicaoY - 1].equals("O"))
				return true;
		}
		if (m[posicaoX][posicaoY + 1].equals("O"))
			return true;

		return false;
	}

	private void criaAgente() {
		Random random = new Random();
		boolean agenteCriado = false;

		while (!agenteCriado) {
			int posicaoRandomX = random.nextInt(tamanhoX);
			int posicaoRandomY = random.nextInt(tamanhoY - 1);

			if (m[posicaoRandomX][posicaoRandomY].equals("-")) {
				m[posicaoRandomX][posicaoRandomY] = "A";
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
		if (m[getPosicaoXAgente() - 1][getPosicaoYAgente()].equals("-")) {
			m[getPosicaoXAgente()][getPosicaoYAgente()] = "-";
			m[getPosicaoXAgente() - 1][getPosicaoYAgente()] = "A";
			setPosicaoXAgente(getPosicaoXAgente() - 1);
			return true;
		}
		return false;
	}

	private boolean moveAgenteParaBaixo() {
		if (m[getPosicaoXAgente() + 1][getPosicaoYAgente()].equals("-")) {
			m[getPosicaoXAgente()][getPosicaoYAgente()] = "-";
			m[getPosicaoXAgente() + 1][getPosicaoYAgente()] = "A";
			setPosicaoXAgente(getPosicaoXAgente() + 1);
			return true;
		}
		return false;
	}

	private boolean moveAgenteParaEsquerda() {
		if (m[getPosicaoXAgente()][getPosicaoYAgente() - 1].equals("-")) {
			m[getPosicaoXAgente()][getPosicaoYAgente()] = "-";
			m[getPosicaoXAgente()][getPosicaoYAgente() - 1] = "A";
			setPosicaoYAgente(getPosicaoYAgente() - 1);
			return true;
		}
		return false;
	}

	private boolean moveAgenteParaDireita() {
		if (m[getPosicaoXAgente()][getPosicaoYAgente() + 1].equals("-")) {
			m[getPosicaoXAgente()][getPosicaoYAgente()] = "-";
			m[getPosicaoXAgente()][getPosicaoYAgente() + 1] = "A";
			setPosicaoYAgente(getPosicaoYAgente() + 1);
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		Labirinto labirinto = new Labirinto();

		String comando = "";

		System.out.println("Bem vindo ao jogo!");
		System.out.println();

		while (true) {
			labirinto.imprimeLabirinto();

			comando = sc.nextLine();

			labirinto.moveAgente(comando);
		}
	}

}
