package model;

public abstract class Objeto {
	
	protected int posicaoX;
	protected int posicaoY;
	protected TipoDeObjeto tipo;
	
	public Objeto(int posicaoX, int posicaoY, TipoDeObjeto tipo) {
		this.posicaoX = posicaoX;
		this.posicaoY = posicaoY;
		this.tipo = tipo;
	}

	public int getPosicaoX() {
		return posicaoX;
	}

	public void setPosicaoX(int posicaoX) {
		this.posicaoX = posicaoX;
	}

	public int getPosicaoY() {
		return posicaoY;
	}

	public void setPosicaoY(int posicaoY) {
		this.posicaoY = posicaoY;
	}

	public TipoDeObjeto getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeObjeto tipo) {
		this.tipo = tipo;
	}

}
