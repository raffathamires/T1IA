package controller;

import java.util.Random;

public class Genetico {

	
	static int[] carga = {};
	
	public static void main(String[] args) {
		int [][]populacao = new int[5][17];
		int [][]intermediaria = new int[5][17];
		
		popular(populacao);
		
		for (int i=0;i<5;i++) {
			System.out.println("Geracao " + i);
			aptidar(populacao);
			printPopulacao(populacao,17);
	
			elitizar(populacao, intermediaria);

			gerar(populacao, intermediaria);
		}
	}
	
	static void clonar(int [][]destino, int [][]origem) {
		for (int i=0; i<5; i++) {
			for (int j=0; j<11; j++) {
				destino[i][j] = origem[i][j];
			}
		}
	}
	
	static void clonar(int []destino, int []origem) {
		for (int j=0; j<11; j++) {
			destino[j] = origem[j];
		}
	}
	
	static void gerar(int [][]populacao, int [][]intermediaria) {
		int linha = 0;
		for (int i=0; i<2; i++) {
			int pai = torner(populacao);
			int mae = torner(populacao);
			linha++;
			for (int j=0; j<5; j++) {
				intermediaria[linha][j] = populacao[pai][j];
				intermediaria[linha+1][j] = populacao[mae][j];
			}
			for (int j=5; j<17; j++) {
				intermediaria[linha][j] = populacao[mae][j];
				intermediaria[linha+1][j] = populacao[pai][j];
			}
			linha++;
		}
		clonar(populacao,intermediaria);
	}
	
	static int torner(int [][]populacao) {
		Random r = new Random();
		int primeiro = r.nextInt(5);
		int segundo = r.nextInt(5);
		return (populacao[primeiro][17] < populacao[segundo][17]) ? primeiro : segundo;
	}
	
	static void elitizar(int [][]populacao, int [][]intermediaria) {
		int indexMenor = 0;
		for (int i=0; i<5; i++) {
			if (populacao[i][17] < populacao[indexMenor][17]) {
				indexMenor = i;
			}
		}
		clonar(intermediaria[0], populacao[indexMenor]);
	}
	
	static void aptidar(int [][]populacao) {
		for (int i=0; i<5; i++) {
			for (int j=0; j<17; j++) {
				populacao[i][17] += (populacao[i][j]==1) ? carga[j] : -carga[j] ;
			}
			populacao[i][17] = Math.abs(populacao[i][17]);
		}
	}

	static void popular(int [][]populacao) {
		Random r = new Random();
		for (int i=0; i<5; i++)
			for (int j=0; j<17; j++) {
				populacao[i][j] = r.nextInt(2);
			}
	}
	
	static void printPopulacao(int [][]populacao, int limite) {
		for (int i=0; i<5; i++) {
			for (int j=0; j<limite; j++) {
				System.out.print(populacao[i][j] + " ");
			}
			System.out.println("");
		}
	}
}
