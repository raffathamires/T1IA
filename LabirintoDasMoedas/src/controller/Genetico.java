package controller;

import java.util.List;
import java.util.Random;
import model.SacoDeMoedas;

public class Genetico {

	static int[] carga = { 40, 50, 70, 40, 60, 70, 40, 50, 60, 20, 40, 50, 60, 30, 40, 50, 10 };

	static int bau0 = 0;
	static int bau1 = 0;
	static int bau2 = 0;
	static int bau3 = 0;
	static int total = 0;

	public static void main(String[] args) {
		int[][] populacao = new int[5][17];
		int[][] intermediaria = new int[5][17];

		popular(populacao);

		for (int i = 0; i < 10; i++) {
			System.out.println("Geracao " + i);
			aptidar(populacao);
			printPopulacao(populacao, 17);

			elitizar(populacao, intermediaria);

			mutar(populacao);
			gerar(populacao, intermediaria);
			
			
		}
	}

	private static void mutar(int[][] populacao) {
		Random r = new Random();
		int mutacao =0;
		int posicao = 0;
		int bau = 0;
		
		for(int i =0; i < 5; i++) {
			mutacao = r.nextInt(2);
				if(mutacao == 1) {
					
						posicao = r.nextInt(16);
						if(populacao[i][posicao] == 0)
						{
							do{	bau = r.nextInt(4);	}while (bau == 0);
						}
						if(populacao[i][posicao] == 1)
						{
							do{	bau = r.nextInt(4);	}while (bau == 1);
						}
						if(populacao[i][posicao] == 2)
						{
							do{	bau = r.nextInt(4);	}while (bau == 2);
						}
						if(populacao[i][posicao] == 3)
						{
							do{	bau = r.nextInt(4);	}while (bau == 3);
						}
					}
				}
		}
		

	private static void iniciarCarga(List<SacoDeMoedas> sacosDeMoedas) {

		// PARA PEGAR OS VALORES DO SACO DE MOEDA E GERAR A CARGA
		//carga = ... 

	}

	static void clonar(int[][] destino, int[][] origem) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 16; j++) {
				destino[i][j] = origem[i][j];
			}
		}
	}

	static void clonarElitizar(int[] destino, int[] origem) {
		for (int j = 0; j < 16; j++) {
			destino[j] = origem[j];
		}
	}

	static void gerar(int[][] populacao, int[][] intermediaria) {
		
		int linha = 0;
		for (int i=0; i<2; i++) {
			int pai = torneio(populacao);
			int mae = torneio(populacao);
			linha++;
			for (int j=0; j<8; j++) {
				intermediaria[linha][j] = populacao[pai][j];
				intermediaria[linha+1][j] = populacao[mae][j];
			}
			for (int j=8; j<16; j++) {
				intermediaria[linha][j] = populacao[mae][j];
				intermediaria[linha+1][j] = populacao[pai][j];
			}
			linha++;
		}
		clonar(populacao,intermediaria);
	}

	static int torneio(int[][] populacao) {
		Random r = new Random();
		int primeiro = r.nextInt(4);
		int segundo = r.nextInt(4);
		return (populacao[primeiro][16] > populacao[segundo][16]) ? primeiro : segundo;
	}

	static void elitizar(int[][] populacao, int[][] intermediaria) {
		int indexMenor = 0;
		for (int i = 0; i < 5; i++) {
			if (populacao[i][16] > populacao[indexMenor][16]) {
				indexMenor = i;
			}
		}
		clonarElitizar(intermediaria[0], populacao[indexMenor]);
	}

	static void aptidar(int[][] populacao) {
		for (int i = 0; i < 5; i++) {
			populacao[i][16] = 0;
			int cont = 0;
			for (int j = 0; j < 16; j++) {
				if (populacao[i][j] == 0) {
					bau0 += carga[j];
				}
				if (populacao[i][j] == 1) {
					bau1 += carga[j];
				}
				if (populacao[i][j] == 2) {
					bau2 += carga[j];
				}
				if (populacao[i][j] == 3) {
					bau3 += carga[j];
				}
			}

			total = bau0 + bau1 + bau2 + bau3;

			if (total / 4 == bau0) {
				cont++;
			}
			if (total / 4 == bau1) {
				cont++;
			}
			if (total / 4 == bau2) {
				cont++;
			}
			if (total / 4 == bau3) {
				cont++;
			}
			
			bau0 = 0;
			bau1 = 0;
			bau2 = 0;
			bau3 = 0;

			populacao[i][16] = cont;
		}
	}

	static void popular(int[][] populacao) {
		Random r = new Random();
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 16; j++) {
				populacao[i][j] = r.nextInt(4);
			}
	}

	static void printPopulacao(int[][] populacao, int limite) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < limite; j++) {
				System.out.print(populacao[i][j] + " ");
			}
			System.out.println("");
		}
	}
}
