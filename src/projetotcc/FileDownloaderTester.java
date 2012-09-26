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

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class FileDownloaderTester extends JFrame {

	private static final long serialVersionUID = -1587506766237847994L;
	private JPanel contentPane;
	private JTextField txtHttp;
	private JButton btnIniciar;
	private JLabel lblNewLabel;
	private JProgressBar progressBar;
	private FileDownloader downloader;
	private Thread dwnThread;
	private static FileDownloaderTester frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					frame = new FileDownloaderTester();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public FileDownloaderTester() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		progressBar = new JProgressBar();
		contentPane.add(progressBar, BorderLayout.CENTER);
		
		lblNewLabel = new JLabel("0/0B");
		contentPane.add(lblNewLabel, BorderLayout.SOUTH);
		
		btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnIniciar.setEnabled(false);
				txtHttp.setEnabled(false);
				downloader = new FileDownloader(txtHttp.getText(), "apagueme");
				try {
					downloader.start();
				} catch (Exception e) {
					e.printStackTrace();
					btnIniciar.setEnabled(true);
					txtHttp.setEnabled(true);
					return;
				}
				if(dwnThread != null)
					dwnThread.interrupt();
				dwnThread = new Thread(){
					@Override
					public void run() {
					  boolean breaked = false;
					  while(!breaked){
						try{
							breaked = downloader.tick();
						}catch(Exception e){
							e.printStackTrace();
							System.exit(-1);
						}
						Integer percent = new Double(downloader.getPosition().doubleValue() / downloader.getSize().doubleValue() * 100).intValue();
						lblNewLabel.setText(
								downloader.getPosition() + "/" +
								downloader.getSize() + "B " +
								percent + "% " + (breaked? "Concluído!" : "Baixando...") );
						progressBar.setValue(percent);
						if(breaked){
							btnIniciar.setEnabled(true);
							txtHttp.setEnabled(true);
							this.interrupt();
						}
					  }
					}
				};
				dwnThread.start();
			}
		});
		contentPane.add(btnIniciar, BorderLayout.EAST);
		
		txtHttp = new JTextField();
		txtHttp.setText("http://fisica.ufpr.br/kurumin/kurumin.iso");
		contentPane.add(txtHttp, BorderLayout.NORTH);
		txtHttp.setColumns(10);
	}

}