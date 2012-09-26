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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainVisual extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6781605021975440639L;
	private JPanel contentPane, panel;
	private JLabel eraserImg;
	private JLabel penImg;
	//private JButton btnBackup;

	/**
	 * Create the frame.
	 */
	public MainVisual() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				if(panel != null)
				panel.setBounds((contentPane.getWidth() - panel.getWidth()) / 2,
						(contentPane.getHeight() - panel.getHeight()) / 2,
						panel.getWidth(), panel.getHeight());
				if(eraserImg != null)
				eraserImg.setBounds(contentPane.getWidth() - eraserImg.getWidth(),
						contentPane.getHeight() - eraserImg.getHeight(),
						eraserImg.getWidth(), eraserImg.getHeight());
				if(penImg != null)
				penImg.setBounds((panel.getX() - penImg.getWidth()),
						panel.getY(), penImg.getWidth(), penImg.getHeight());
			}
		});
		setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 440, 320);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(129, 11, 149, 142);
        contentPane.add(panel);
        panel.setLayout(null);
        
        JButton btnGoTraining = new JButton("Iniciar Treinamento");
        btnGoTraining.setBounds(0, 0, 149, 40);
        panel.add(btnGoTraining);
        btnGoTraining.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent arg0) {
        		Main.changeVisualTo(new ChooseTest());
        	}
        });
        btnGoTraining.setMnemonic('T');
        
        JButton btnSeeStatus = new JButton("Ver Estatisticas");
        btnSeeStatus.setBounds(0, 51, 149, 40);
        panel.add(btnSeeStatus);
        btnSeeStatus.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent arg0) {
        		Main.changeVisualTo(new Statistics());
        	}
        });
        btnSeeStatus.setMnemonic('S');
        
        JButton btnExit = new JButton("Sair");
        btnExit.setBounds(0, 102, 149, 37);
        panel.add(btnExit);
        btnExit.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent arg0) {
        		System.exit(0);
        	}
        });
        btnExit.setMnemonic('E');
        
        /* btnExit.setBounds(0, 102, 149, 40);
        btnBackup = new JButton("Backup");
        btnBackup.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Main.changeVisualTo(new DataManager());
        	}
        });
        btnBackup.setBounds(0, 102, 149, 37);
        panel.add(btnBackup);*/
        
        URL eraseURL = getClass().getResource("/res/eraser.png");
        eraserImg = new JLabel(new ImageIcon( eraseURL ));
        eraserImg.setBounds(282, 102, 135, 120);
        contentPane.add(eraserImg);
        
        penImg = new JLabel(new ImageIcon( this.getClass().getResource("/res/pen.png") ));
        penImg.setBounds(64, 26, 65, 142);
        contentPane.add(penImg);
	}
}
