package br.com.robertosantin.view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.robertosantin.model.BotaoCampo;
import br.com.robertosantin.model.Tabuleiro;

public class PainelTabuleiro extends JPanel {

	private static final long serialVersionUID = 7829344384008260674L;

	public PainelTabuleiro(Tabuleiro tabuleiro) {

		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

		tabuleiro.getCampos().forEach(c -> add(new BotaoCampo(c)));

		tabuleiro.registrarObservador(e -> {

			SwingUtilities.invokeLater(() -> {

				if (e) {
					JOptionPane.showMessageDialog(this, "Ganhou :)");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu :(");
				}
				
				tabuleiro.reiniciar();
			});
		});
	}

}
