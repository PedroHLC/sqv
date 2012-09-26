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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class ChooseTest extends JFrame {

	private static final long serialVersionUID = -7820752752710030254L;
	private JPanel contentPane;
	
	//private JList<String> lstTests;
	private Connection sqlConn;
	private JTable testsTable;
	private TableColumn colUse;
	private TableColumn colIdent;
	private DefaultTableModel testsTableModel;
	private Vector<String> testsSNames, choosedones;
	private JButton btnGo;
	private JProgressBar progressBar;
	private JProgressBar progressBar1;
	private boolean tmpSucess;
	private int tmpTestIndex = 0,
			tmpTestsNum = 0;

	/**
	 * Create the frame.
	 */
	public ChooseTest() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 282);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnGo = new JButton("Ir");
		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startDownload();
			}
		});
		btnGo.setMnemonic('G');
		btnGo.setBounds(220, 176, 118, 23);
		contentPane.add(btnGo);
		
		JButton btnBack = new JButton("Voltar");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.changeVisualTo(new MainVisual());
			}
		});
		btnBack.setMnemonic('E');
		btnBack.setBounds(10, 176, 89, 23);
		contentPane.add(btnBack);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 328, 154);
		contentPane.add(scrollPane);
		
		testsTableModel = new DefaultTableModel(){
		    private static final long serialVersionUID = -3085626163435318625L;
			@Override
			public Class<?> getColumnClass(int column) {  
		        if (column == 0)  
		           return Boolean.class;  
		        return super.getColumnClass(column);  
		    }
		};
		testsTableModel.addColumn("Usar?");
		testsTableModel.addColumn("Identificação?");
		testsTable = new JTable();
		testsTable.setModel(testsTableModel);
		testsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		testsTable.setSelectionBackground(testsTable.getBackground());
		testsTable.setSelectionForeground(testsTable.getForeground());
		
		colUse = testsTable.getColumn("Usar?");
		colUse.setMaxWidth(48);
		colUse.setMinWidth(32);
		JCheckBox colBox = new JCheckBox();
		colBox.setHorizontalAlignment(SwingConstants.CENTER);
		colUse.setCellEditor(new DefaultCellEditor(colBox));
		colUse.setCellRenderer(null);
		colIdent = testsTable.getColumn("Identificação?");
		colIdent.setCellEditor(new DefaultCellEditor(new JTextField()){
			private static final long serialVersionUID = -6236634490450703472L;
			@Override public boolean isCellEditable(EventObject anEvent) {
				return false;	}
		});
		
		scrollPane.setViewportView(testsTable);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(54, 209, 284, 14);
		contentPane.add(progressBar);
		
		progressBar1 = new JProgressBar();
		progressBar1.setBounds(54, 230, 284, 14);
		contentPane.add(progressBar1);
		
		JLabel lblNewLabel = new JLabel("Atual:");
		lblNewLabel.setBounds(10, 205, 70, 18);
		contentPane.add(lblNewLabel);
		
		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setBounds(10, 230, 70, 18);
		contentPane.add(lblTotal);
		
		new Thread(){
			@Override
			public void run(){
				prepareList();
				System.out.println("...");
			}
		}.start();
	}
	
	
	protected void prepareList(){
		try {
			FileDownloader.instantDownloadFile(Main.webSQVAddr + Main.sqlTestsListDBWeb, Main.getDBFilePath(Main.sqlTestsListDB)); //*WEB
		} catch (Exception e) {
			Main.showMessage("Não foi possível se conectar a internet ou nosso website não está disponível.", Main.titleError);
			e.printStackTrace();
		}

		sqlConn = null;
		sqlConn = Main.createSQLConn(Main.sqlTestsListDB);
		if(sqlConn != null)
			try {
				Statement stat = sqlConn.createStatement();
				String query = "SELECT `name` from `" + Main.sqlTestsTable + "`;";
				ResultSet result =  stat.executeQuery(query);
				if (result.getMetaData().getColumnCount() <= 0){
					Main.showMessage("Lista de provas indisponível, por favor, conecte o computador a internet ao menos uma vez.", "Aviso");
					return;
				}
				while(result.next()){
					testsTableModel.addRow(new Object[]{Boolean.FALSE, result.getString(1)});
				}
				testsTable.setModel(testsTable.getModel());
				stat.clearBatch();
				testsSNames = new Vector<String>();
				query = "SELECT `namespace` from `" + Main.sqlTestsTable + "`;";
				result =  stat.executeQuery(query);
				if (result.getMetaData().getColumnCount() <= 0){
					Main.showMessage("Lista de provas indisponível, por favor, conecte o computador a internet ao menos uma vez.", "Aviso");
					return;
				}
				while(result.next()){
					testsSNames.add(result.getString(1));
				}
				stat.close();
			} catch (SQLException e) {
				Main.showMessage("Lista de provas indisponível, por favor, conecte o computador a internet ao menos uma vez.", "Aviso");
				e.printStackTrace();
			}
	}
	
	private void startDownload(){
		this.choosedones = new Vector<String>();
		for(int i = 0; i < testsSNames.size(); i++)
			if((Boolean)testsTable.getValueAt(i, 0))
				choosedones.add(testsSNames.get(i));
		tmpTestsNum = choosedones.size();
		if(tmpTestsNum < 1){
			Main.showMessage("Selecione pelo menos uma prova.", this.getTitle());
			return;
		}
		btnGo.setEnabled(false);
		testsTable.setEnabled(false);
		btnGo.setText("Baixando...");
		progressBar1.setValue(0);
		progressBar1.setMaximum(tmpTestsNum * 100);
		tmpSucess = true;
		new Thread(){
			@Override
			public void run(){
				boolean breaked;
				for(tmpTestIndex = 0; tmpTestIndex < tmpTestsNum; tmpTestIndex++){
					String testSName = choosedones.get(tmpTestIndex) + Main.sqlSufix;
					System.out.println("Downloading Test: "+testSName);
					String foutpath = Main.dataFolder + Main.sqlTestsDBFolder + testSName;
					String inpath = Main.webSQVAddr + Main.sqlTestsDBFolderWeb + testSName; //WEB*
					if(!new File(foutpath).exists()){
						String errorMessage = "Erro ao tentar baixar uma prova.";
						String errorTitle = "Erro.";
						FileDownloader downloader = new FileDownloader(inpath, foutpath); //WEB*
						try {
							downloader.start();
							breaked = false;
							while(!breaked){
								breaked = downloader.tick();
								Integer percent = new Double(downloader.getPosition().doubleValue() / downloader.getSize().doubleValue() * 100).intValue();
								progressBar.setValue(percent);
								progressBar1.setValue((tmpTestIndex * 100) + percent);	
							}
							tmpSucess = true;
						} catch (MalformedURLException e) {
							Main.showMessage(errorMessage, errorTitle);
							e.printStackTrace();
						} catch (IOException e) {
							Main.showMessage(errorMessage, errorTitle);
							e.printStackTrace();
						}
					}
				}
				progressBar1.setValue((tmpTestIndex * 100) + 100);
				finishDownload();
			}
		}.start();
	}
	
	private void finishDownload(){
		if(tmpSucess){
			QuestionsRunner.doTests(choosedones);
		}else{
			btnGo.setEnabled(true);
			testsTable.setEnabled(true);
			btnGo.setText("Ir...");
		}
	}
}