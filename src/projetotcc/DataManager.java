/**
    Copyright (C) 2012 Pedro Henrique Lara Campos, Felipe Rodrigues Varjão

    This file is part of SQV.

    SQV is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    SQV is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with SQV.  If not, see <http://www.gnu.org/licenses/>.
 */

package projetotcc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DataManager extends JFrame {

	private static final long serialVersionUID = 8468243030218582209L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public DataManager() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 340, 228);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnSalvarResultados = new JButton("Salvar resultados");
		btnSalvarResultados.setBounds(99, 11, 135, 23);
		contentPane.add(btnSalvarResultados);
		
		JButton btnRestaurarResultados = new JButton("Restaurar resultados");
		btnRestaurarResultados.setBounds(99, 45, 135, 23);
		contentPane.add(btnRestaurarResultados);
		
		JButton btnSalvarProvas = new JButton("Salvar provas");
		btnSalvarProvas.setBounds(99, 79, 135, 23);
		contentPane.add(btnSalvarProvas);
		
		JButton btnRestaurarProvas = new JButton("Restaurar provas");
		btnRestaurarProvas.setBounds(99, 113, 135, 23);
		contentPane.add(btnRestaurarProvas);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.changeVisualTo(new MainVisual());
			}
		});
		btnVoltar.setBounds(122, 166, 89, 23);
		contentPane.add(btnVoltar);
	}
}
