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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import third.ChartPanel;

public class Statistics extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7450707632169218994L;
	private JPanel contentPane;
	private Connection conn;
	public static final String tableCreateQuery = "CREATE TABLE IF NOT EXISTS history(date DATE NOT NULL,corrects INT NOT NULL,wrongs INT NOT NULL,emptys INT NOT NULL);";
	private JLabel lblAcertos, lblErradas, lblNoRespondidas, lblHighest;
	private JPanel panel;
	private ChartPanel panel_1;
	private StatisticsTable table;
	
	private void connectSQL(){
		if(conn != null)
			try {
				conn.close();
			} catch (SQLException ei) {};
		conn = Main.createSQLConn(Main.sqlHistoryDB);
		try {
			Statement stat = conn.createStatement();
			stat.executeUpdate(tableCreateQuery);
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Main.showMessage(e.toString(), "ERRO");
			System.exit(-1);
		}
	}

	private void loadHistory(){
		Statement stat;
		try {
			stat = conn.createStatement();
			ResultSet result = stat.executeQuery("SELECT * FROM history;");
			panel_1 = createChartPanel(result);
			panel_1.setLayout(null);
			
			/*JPanel chartPanel = new JPanel();
			chartPanel.setBackground(Color.WHITE);
			chartPanel.setBounds(48, 1, 376, 129);
			panel_1.add(chartPanel);*/
			result.close();
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the frame.
	 */
	private void inti(){
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 36, 424, 131);
		contentPane.add(panel);
		panel.setLayout(null);
		
		if(panel_1 == null)
			panel_1 = createChartPanel(null);
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(45, 1, 378, 129);
		panel.add(panel_1);
		
		if(lblHighest == null)
			lblHighest = new JLabel("000 -");
		lblHighest.setBounds(2, 18, 70, 15);
		panel.add(lblHighest);
		
		JLabel lblLower = new JLabel("0 -");
		lblLower.setBounds(2, 108, 70, 15);
		panel.add(lblLower);
		
		/*JLabel lblQuantidadeDeTentativas = new JLabel("Conciderar apenas os \u00FAltimos:");
		lblQuantidadeDeTentativas.setBounds(237, 11, 144, 14);
		contentPane.add(lblQuantidadeDeTentativas);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(391, 8, 43, 20);
		spinner.setValue(7);
		contentPane.add(spinner);*/
		
		if(lblAcertos == null)
			lblAcertos = new JLabel();
		lblAcertos.setBounds(10, 178, 132, 14);
		contentPane.add(lblAcertos);
		
		if(lblErradas == null)
			lblErradas = new JLabel();
		lblErradas.setBounds(10, 203, 132, 14);
		contentPane.add(lblErradas);
		
		if(lblNoRespondidas == null)
			lblNoRespondidas = new JLabel();
		lblNoRespondidas.setBounds(202, 178, 132, 14);
		contentPane.add(lblNoRespondidas);
		
		JButton btnClean = new JButton("Limpar");
		btnClean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Statement stat = conn.createStatement();
					stat.executeUpdate("DELETE FROM history;");
					stat.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
					Main.showMessage(e1.toString(), "ERRO");
					System.exit(-1);
				}
			}
		});
		btnClean.setBounds(55, 238, 103, 23);
		contentPane.add(btnClean);
		
		JButton btnIrATela = new JButton("Ir a tela inicial");
		btnIrATela.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.changeVisualTo(new MainVisual());
			}
		});
		btnIrATela.setBounds(168, 238, 99, 23);
		contentPane.add(btnIrATela);
		
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});
		btnSair.setBounds(277, 238, 89, 23);
		contentPane.add(btnSair);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBackground(Color.RED);
		panel_2.setBounds(10, 12, 64, 22);
		contentPane.add(panel_2);
		
		JLabel lblAcertos_1 = new JLabel("Acertos");
		lblAcertos_1.setBounds(79, 16, 70, 15);
		contentPane.add(lblAcertos_1);
		
		JButton btnVerEmTabela = new JButton("Ver em tabela");
		btnVerEmTabela.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.newVisual(table);
			}
		});
		btnVerEmTabela.setBounds(305, 9, 133, 25);
		contentPane.add(btnVerEmTabela);
	}
	
	public Statistics() {
		table = new StatisticsTable();
		connectSQL();
		loadHistory();
		inti();
	}
	
	public Statistics(Vector<Byte> correctsls, Vector<Byte> chooseds) {
		table = new StatisticsTable();
		int len = correctsls.size(),
				corrects = 0,
				wrongs = 0,
				emptys = 0;
		byte value;
		for(int i=0; i<len; i++){
			value = chooseds.get(i);
			if(value < 6 & value >= 0){
				if(value == (correctsls.get(i) - 1))
					corrects++;
				else
					wrongs++;
			}else
				emptys++;
		}
		connectSQL();
		Statement stat;
	
		String date = Main.getTodayDate();
		
		try {
			stat = conn.createStatement();
			stat.executeUpdate("INSERT INTO history VALUES(\""+date+"\", "+corrects+","+wrongs+","+emptys+");");
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadHistory();
		inti();
	}
	

	private ChartPanel createChartPanel(ResultSet rs){
		Vector<Double> values = new Vector<Double>();
		Vector<String> labels = new Vector<String>();
	    
		int corrects = 0, wrongs = 0, emptys = 0, total, realtotal, highest = 0;
		
		if(rs != null)
			try {
				while(rs.next()){
					String date = rs.getString(1);
					labels.add(date);
					int tcorrects = rs.getInt(2),
						twrongs = rs.getInt(3),
						temptys = rs.getInt(4);
					values.add((double)tcorrects);
					corrects += tcorrects;
					wrongs += twrongs;
					emptys += temptys;
					if(tcorrects > highest)
						highest = tcorrects;
					table.insert(date, tcorrects, twrongs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Main.showMessage("Não foi possível ler o histórico de resultados", "SQV - ERRO");
			}
		
		total =  corrects + wrongs;
		realtotal = total + emptys;
		
		if(lblAcertos == null)
			lblAcertos = new JLabel("Acertou: "+corrects + "/" + total);
		else
			lblAcertos.setText("Acertou: "+corrects + "/" + total);
		
		if(lblErradas == null)
			lblErradas = new JLabel("Errou: "+wrongs + "/" + total);
		else
			lblErradas.setText("Errou: "+wrongs + "/" + total);
		
		if(lblNoRespondidas == null)
			lblNoRespondidas = new JLabel("Pulou: "+emptys + "/" + realtotal);
		else
			lblNoRespondidas.setText("Pulou: "+emptys + "/" + realtotal);
		
		if(lblHighest == null)
			lblHighest = new JLabel(highest+" -");
		else
			lblHighest.setText(highest+" -");
	    return new ChartPanel(values, labels, "Últimos resultados.");
	}
}
