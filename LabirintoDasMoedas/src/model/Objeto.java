package model;

import java.util.HashSet;
import java.util.Set;

public abstract class Objeto {

	protected Coordenadas coordenadas;
	protected TipoDeObjeto tipo;
	protected Objeto objetoNorte;
	protected Objeto objetoSul;
	protected Objeto objetoLeste;
	protected Objeto objetoOeste;
	protected Double distanciaDoAgente;
	protected Double distanciaEstimada;
	protected boolean visitado;
	protected boolean conhecido;

	public Objeto(int posicaoX, int posicaoY, TipoDeObjeto tipo) {
		this.coordenadas = new Coordenadas(posicaoX, posicaoY);
		this.tipo = tipo;
		this.objetoLeste = null;
		this.objetoOeste = null;
		this.objetoNorte = null;
		this.objetoSul = null;
		this.distanciaDoAgente = new Double(0);
		this.distanciaEstimada = new Double(0);
		this.visitado = false;
		this.conhecido = false;
	}

	public int getPosicaoX() {
		return this.coordenadas.getPosicaoX();
	}

	public void setPosicaoX(int posicaoX) {
		this.coordenadas.setPosicaoX(posicaoX);
	}

	public int getPosicaoY() {
		return this.coordenadas.getPosicaoY();
	}

	public void setPosicaoY(int posicaoY) {
		this.coordenadas.setPosicaoY(posicaoY);
	}

	public Coordenadas getCoordenadas() {
		return this.coordenadas;
	}

	public void setCoordenadas(Coordenadas coordenadas) {
		this.coordenadas = coordenadas;
	}

	public TipoDeObjeto getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeObjeto tipo) {
		this.tipo = tipo;
	}

	public Objeto getObjetoNorte() {
		return objetoNorte;
	}

	public void setObjetoNorte(Objeto objetoNorte) {
		this.objetoNorte = objetoNorte;
	}

	public Objeto getObjetoSul() {
		return objetoSul;
	}

	public void setObjetoSul(Objeto objetoSul) {
		this.objetoSul = objetoSul;
	}

	public Objeto getObjetoLeste() {
		return objetoLeste;
	}

	public void setObjetoLeste(Objeto objetoLeste) {
		this.objetoLeste = objetoLeste;
	}

	public Objeto getObjetoOeste() {
		return objetoOeste;
	}

	public void setObjetoOeste(Objeto objetoOeste) {
		this.objetoOeste = objetoOeste;
	}

	public Double getDistanciaDoAgente() {
		return distanciaDoAgente;
	}

	public void setDistanciaDoAgente(Double distanciaDoAgente) {
		this.distanciaDoAgente = distanciaDoAgente;
	}

	public Double getDistanciaEstimada() {
		return distanciaEstimada;
	}

	public void setDistanciaEstimada(Double distanciaEstimada) {
		this.distanciaEstimada = distanciaEstimada;
	}

	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}

	public boolean isConhecido() {
		return conhecido;
	}

	public void setConhecido(boolean conhecido) {
		this.conhecido = conhecido;
	}

	public Set<Objeto> getVizinhos() {
		Set<Objeto> vizinhos = new HashSet<>();

		if (this.objetoLeste != null) {
			vizinhos.add(this.objetoLeste);
		}

		if (this.objetoOeste != null) {
			vizinhos.add(this.objetoOeste);
		}

		if (this.objetoNorte != null) {
			vizinhos.add(this.objetoNorte);
		}

		if (this.objetoSul != null) {
			vizinhos.add(this.objetoSul);
		}

		return vizinhos;
	}
	
	public Set<Objeto> getVizinhosLivres() {
		Set<Objeto> vizinhos = new HashSet<>();

		if (getObjetoNorte() != null && getObjetoNorte().getTipo() != TipoDeObjeto.MURO) {
			vizinhos.add(getObjetoNorte());
		}
		
		if (getObjetoSul() != null && getObjetoSul().getTipo() != TipoDeObjeto.MURO) {
			vizinhos.add(getObjetoSul());
		}
		
		if (getObjetoLeste() != null && getObjetoLeste().getTipo() != TipoDeObjeto.MURO) {
			vizinhos.add(getObjetoLeste());
		}
		
		if (getObjetoOeste() != null && getObjetoOeste().getTipo() != TipoDeObjeto.MURO) {
			vizinhos.add(getObjetoOeste());
		}
	
		return vizinhos;
	}

	public boolean verificaSeVizinhosLivreValidandoNaoVisitado(Objeto objeto, boolean validaNaoVisitado) {
		if (objeto != null && objeto.getTipo() != TipoDeObjeto.MURO) {
			if (validaNaoVisitado && !objeto.visitado) {
				return true;
			} else if (!validaNaoVisitado) {
				return true;
			}
		}
		return false;
	}

	public Set<Objeto> getVizinhosLivresValidandoNaoVisitado(boolean validaNaoVisitado) {
		Set<Objeto> vizinhos = new HashSet<>();

		if (verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoNorte, validaNaoVisitado)) {
			if (this.objetoNorte.getTipo() == TipoDeObjeto.BURACO) {
				if (verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoNorte.objetoNorte, validaNaoVisitado)) {
					vizinhos.add(this.objetoNorte);
				}
			} else {
				vizinhos.add(this.objetoNorte);
			}
		}
		if (verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoSul, validaNaoVisitado)) {
			if (this.objetoSul.getTipo() == TipoDeObjeto.BURACO) {
				if (verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoSul.objetoSul, validaNaoVisitado)) {
					vizinhos.add(this.objetoSul);
				}
			} else {
				vizinhos.add(this.objetoSul);
			}
		}
		if (verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoLeste, validaNaoVisitado)) {
			if (this.objetoLeste.getTipo() == TipoDeObjeto.BURACO) {
				if (verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoLeste.objetoLeste, validaNaoVisitado)) {
					vizinhos.add(this.objetoLeste);
				}
			} else {
				vizinhos.add(this.objetoLeste);
			};
		}
		if (verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoOeste, validaNaoVisitado)) {
			if (this.objetoOeste.getTipo() == TipoDeObjeto.BURACO) {
				if (verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoOeste.objetoOeste, validaNaoVisitado)) {
					vizinhos.add(this.objetoOeste);
				}
			} else {
				vizinhos.add(this.objetoOeste);
			};
		}

		if (vizinhos.isEmpty()) {
			if (this.objetoNorte != null && verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoNorte.objetoNorte, validaNaoVisitado)) {
				vizinhos.add(this.objetoNorte);
			}
			if (this.objetoSul != null && verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoSul.objetoSul, validaNaoVisitado)) {
				vizinhos.add(this.objetoSul);
			}
			if (this.objetoLeste != null && verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoLeste.objetoLeste, validaNaoVisitado)) {
				vizinhos.add(this.objetoLeste);
			}
			if (this.objetoOeste != null && verificaSeVizinhosLivreValidandoNaoVisitado(this.objetoOeste.objetoOeste, validaNaoVisitado)) {
				vizinhos.add(this.objetoOeste);
			}
		}

		return vizinhos;
	}

	public Objeto getVizinhoPelaDirecao(Direcao direcao) {
		if (Direcao.NORTE == direcao)
			return objetoNorte;
		if (Direcao.SUL == direcao)
			return objetoSul;
		if (Direcao.LESTE == direcao)
			return objetoLeste;
		if (Direcao.OESTE == direcao)
			return objetoOeste;
		return null;
	}

	public Direcao getDirecaoVizinho(Objeto vizinho) {
		if (objetoNorte == vizinho)
			return Direcao.NORTE;
		if (objetoSul == vizinho)
			return Direcao.SUL;
		if (objetoLeste == vizinho)
			return Direcao.LESTE;
		if (objetoOeste == vizinho)
			return Direcao.OESTE;
		return null;
	}

	public double calcularDistancia(Objeto vizinho) {
		if (vizinho == null) {
			return Double.POSITIVE_INFINITY;
		}
		return 1;
	}

	public double calcularHeuristica(Objeto objetivo) {
		// RaizQuadrada( (x2 - x1)² + (y2 - y1)² )
		return Math.sqrt(Math.pow((objetivo.getPosicaoX() - this.getPosicaoX()), 2)
				+ Math.pow((objetivo.getPosicaoY() - this.getPosicaoY()), 2));
	}

}
