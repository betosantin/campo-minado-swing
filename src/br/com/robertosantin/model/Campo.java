package br.com.robertosantin.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Campo {

	private final int linha;
	private final int coluna;

	private boolean minado = false;
	private boolean aberto = false;
	private boolean marcado = false;

	private List<Campo> vizinhos = new ArrayList<Campo>();
	private Set<CampoObserver> observadores = new LinkedHashSet<CampoObserver>();

	public Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	public void registrarObservador(CampoObserver observer) {
		observadores.add(observer);
	}
	
	private void notificarObservadores(CampoEvento evento) {
		observadores.stream().forEach( o -> o.eventoOcorreu(this, evento));
	}
	
	
	public boolean isMinado() {
		return minado;
	}

	public void setMinado(boolean minado) {
		this.minado = minado;
	}

	public boolean isAberto() {
		return aberto;
	}

	public void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if(this.aberto) {
			this.notificarObservadores(CampoEvento.ABRIR);
		}
	}

	public boolean isMarcado() {
		return marcado;
	}

	public void setMarcado(boolean marcado) {
		this.marcado = marcado;
	}

	public List<Campo> getVizinhos() {
		return vizinhos;
	}

	public void setVizinhos(List<Campo> vizinhos) {
		this.vizinhos = vizinhos;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	public boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = this.getLinha() != vizinho.getLinha();
		boolean colunaDiferente = this.getColuna() != vizinho.getColuna();

		boolean diagonal = linhaDiferente && colunaDiferente;

		int deltaLinha = Math.abs(this.getLinha() - vizinho.getLinha());
		int deltaColuna = Math.abs(this.getColuna() - vizinho.getColuna());
		int deltaGeral = deltaColuna + deltaLinha;

		if (deltaGeral == 1 && !diagonal) {
			this.getVizinhos().add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			this.getVizinhos().add(vizinho);
			return true;
		}

		return false;

	}

	public void alternarMarcacao() {
		if (!this.isAberto()) {
			this.setMarcado(!this.isMarcado());
			
			if(isMarcado()) {
				this.notificarObservadores(CampoEvento.MARCAR);
			} else {
				this.notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	}

	public boolean abrir() {

		if (!this.isAberto() && !this.isMarcado()) {
			
			if (this.isMinado()) {
				this.notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			this.setAberto(true);
			
			if (this.vizinhancaSegura()) {
				this.getVizinhos().forEach(vizinho -> vizinho.abrir());
			}

			return true;
		}

		return false;
	}

	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.isMinado());
	}

	public boolean objetivoAlcancado() {
		return (this.isAberto() && !this.isMinado()) || (this.isMinado() || this.isMarcado());
	}

	public int minasNasVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.isMinado()).count();
	}

	public void reiniciar() {
		this.setAberto(false);
		this.setMinado(false);
		this.setMarcado(false);
		
		this.notificarObservadores(CampoEvento.REINICIAR);
	}

}
