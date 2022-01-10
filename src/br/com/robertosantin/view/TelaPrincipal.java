package br.com.robertosantin.view;

import javax.swing.JFrame;

import br.com.robertosantin.model.Tabuleiro;

public class TelaPrincipal extends JFrame {

	private static final long serialVersionUID = 8058037900802336932L;

	public TelaPrincipal() {
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);
		add(new PainelTabuleiro(tabuleiro));
		
		setTitle("Campo Minado");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(690, 440);
		setVisible(true);
	}

	public static void main(String[] args) {

		new TelaPrincipal();

	}

}
