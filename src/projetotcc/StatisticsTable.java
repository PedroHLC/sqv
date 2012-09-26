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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class StatisticsTable extends JFrame {

	private static final long serialVersionUID = -1605545629232516402L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel tableModel;

	/**
	 * Create the frame.
	 */
	public StatisticsTable() {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		table = new JTable();
		table.setRowSelectionAllowed(false);
		tableModel = new DefaultTableModel(
			new Object[][] {
				//{null, null, null},
			},
			new String[] {
				"Data", "Corretas", "Erradas"
			}
		) {
			private static final long serialVersionUID = -1020842771606879609L;
			Class<?>[] columnTypes = new Class<?>[] {
				String.class, Integer.class, Integer.class
			};
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setBorder(new LineBorder(Color.BLACK));
		tableScroll.setBounds(12, 12, 426, 193);
		contentPane.add(tableScroll);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnOk.setBounds(161, 217, 117, 25);
		contentPane.add(btnOk);
		
		this.setResizable(false);
	}
	
	public void insert(String date, Integer corrects, Integer wrongs){
		tableModel.addRow(new Object[]{date, corrects, wrongs});
		table.setModel(tableModel);
	}
}
