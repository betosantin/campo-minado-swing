package br.com.robertosantin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Tabuleiro implements CampoObserver {

	private int linhas;
	private int colunas;
	private int minas;

	private final List<Campo> campos = new ArrayList<Campo>();
	
	private final List<Consumer<Boolean>> observadores = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;

		this.gerarCampos();
		this.associarVizinhos();
		this.sortearMinas();
	}
	
	public void registrarObservador(Consumer<Boolean> observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(Boolean resultado) {
		observadores.stream().forEach( o -> o.accept(resultado));
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public int getMinas() {
		return minas;
	}

	public List<Campo> getCampos() {
		return campos;
	}

	public void abrirCampo(int linha, int coluna) {
		this.getCampos().stream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
		.ifPresent(c -> c.abrir());

	}
	
	private void mostrarMinas() {
		this.getCampos().stream()
			.filter(c -> c.isMinado())
			.filter(c -> !c.isMarcado())
			.forEach( d -> d.setAberto(true));
	}
	
	public void marcarCampo(int linha, int coluna) {

		this.getCampos().stream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.alternarMarcacao());

	}

	private void gerarCampos() {
		for (int i = 0; i < this.getLinhas(); i++) {
			for (int j = 0; j < this.getColunas(); j++) {
				Campo campo = new Campo(i, j);
				campo.registrarObservador(this);
				
				this.getCampos().add(campo);
			}
		}
	}

	private void associarVizinhos() {
		for (Campo c1 : this.getCampos()) {
			for (Campo c2 : this.getCampos()) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void sortearMinas() {
		long minasArmadas = 0;

		do {
			int aleatorio = (int) (Math.random() * this.getCampos().size());
			this.getCampos().get(aleatorio).setMinado(true);
			
			minasArmadas = this.getCampos().stream().filter(c -> c.isMinado()).count();

		} while (minasArmadas < this.getMinas());
	}

	public boolean objetivoAlcancado() {
		return this.getCampos().stream().allMatch(c -> c.objetivoAlcancado());
	}

	public void reiniciar() {
		this.getCampos().forEach(c -> c.reiniciar());
		this.sortearMinas();
	}

	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {

		if(evento == CampoEvento.EXPLODIR) {
			this.mostrarMinas();
			this.notificarObservadores(false);
		} else if(this.objetivoAlcancado()) {
			this.notificarObservadores(true);
		}
	}

}
