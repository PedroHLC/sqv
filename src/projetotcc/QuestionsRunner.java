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
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class QuestionsRunner extends JFrame {

	private static final long serialVersionUID = -2226626690205913640L;
	private JPanel contentPane, panel;
	protected JScrollPane scrollPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextArea txtQuestao;
	private Byte correctop = 0;
	private Vector<JRadioButton> radiobtns;
	private Vector<Byte> choosedsAnswers, correctsAnswers;
	private Vector<Test> tests = null;
	private int questindex;
	private Vector<QuestionID> questions;
	private RowSpec[] rowSpecDefault;
	private ColumnSpec[] colSpecDefault;
	private JButton btnGo, btnBack;
	public static final String[] optionsLabel = {"A) ", "B) ", "C) ", "D) ", "E) ", "F) "};
	
	private void whenResized(){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(txtQuestao == null) return;
				setQuestionData("Carregando", "Espere...", new Vector<String>(), (byte) 0); 
				txtQuestao.setColumns((getSize().width - 14) / txtQuestao.getFont().getSize());
				if(tests != null){
					QuestionID actquest = questions.get(questindex);
					setQuestionData(tests.get(actquest.test).getQuestion(actquest.id));
				}
			}
		});
	}
	
	public static Vector<QuestionID> shuffleTests(Vector<Test> ltests){
		Vector<QuestionID> qs = new Vector<QuestionID>();
		for(int t=0; t<ltests.size(); t++){
			Test test = ltests.get(t);
			for(int i=1; i<test.getQuestionsNum(); i++)
				qs.add(new QuestionID(t, i));
		}
		Collections.shuffle(qs);
		return qs;
	}
	
	public QuestionsRunner(Vector<Test> ltests){
		this.tests = ltests;
		this.questions = shuffleTests(this.tests);
		this.choosedsAnswers = new Vector<Byte>();
		this.correctsAnswers = new Vector<Byte>();
		for(int i = 0; i <=  this.questions.size(); i++){
			this.choosedsAnswers.add((byte) 255);
			this.correctsAnswers.add((byte) 255);
		}
		initialize();
		this.questindex = 0;
		QuestionID actquest = this.questions.get(this.questindex);
		this.setQuestionData(tests.get(actquest.test).getQuestion(actquest.id));
	}
	
	public QuestionsRunner(){
		this.tests = null;
		this.choosedsAnswers = null;
		this.correctsAnswers = null;
		this.questions = null;
		this.questindex = 0;
		initialize();
	}
	
	private void checkOptionStatus(){
		if(this.choosedsAnswers == null)
			return;
		byte choosed = (byte)255;
		for(byte r=0; r<6; r++)
			if(this.radiobtns.get(r).isSelected()){
				choosed = r;
				break;
			}
		this.choosedsAnswers.set(questindex, choosed);
		this.correctsAnswers.set(questindex, correctop);
	}
	
	/**
	 * Create the frame.
	 */
	public void initialize() {
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				whenResized();
			}
		});		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 610, 457);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelbotoes = new JPanel();
		contentPane.add(panelbotoes, BorderLayout.SOUTH);
		
		btnBack = new JButton("Voltar");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkOptionStatus();
				questindex -= 1;
				QuestionID actquest = questions.get(questindex);
				setQuestionData(tests.get(actquest.test).getQuestion(actquest.id));
			}
		});
		
		btnGo = new JButton("Avan\u00E7ar");
		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkOptionStatus();
				questindex += 1;
				QuestionID actquest = questions.get(questindex);
				setQuestionData(tests.get(actquest.test).getQuestion(actquest.id));
			}
		});
			
		panelbotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelbotoes.add(btnBack);
		
		JButton btnCheck = new JButton("Verificar Resposta");
		btnCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkOptionStatus();
				JRadioButton rightbtn = radiobtns.get(correctop-1);
				rightbtn.setForeground(Color.GREEN);
			}
		});
		panelbotoes.add(btnCheck);
		
		JButton btnTerminar = new JButton("Terminar");
		btnTerminar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkOptionStatus();
				//correctsNum, test
				Main.changeVisualTo(new Statistics(correctsAnswers, choosedsAnswers));
			}
		});
		panelbotoes.add(btnTerminar);
		panelbotoes.add(btnGo);
		
		scrollPane = new JScrollPane();
		//scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		scrollPane.setViewportView(panel);
		
		rowSpecDefault = new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,};
		
		colSpecDefault = new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC
				};
		
		panel.setLayout(new FormLayout(colSpecDefault, rowSpecDefault));
		
		txtQuestao = new JTextArea();
		txtQuestao.setEditable(false);
		txtQuestao.setLineWrap(true);
		txtQuestao.setWrapStyleWord(true);
		txtQuestao.setFont(new Font("Arial", Font.BOLD, 12));
		txtQuestao.setColumns((this.getSize().width - 8) / txtQuestao.getFont().getSize());
		txtQuestao.setText("(Fonte) Questao");
		panel.add(txtQuestao, "2, 2, fill, top");
		
		JLabel label = new JLabel("");
		panel.add(label, "2, 2, left, center");
		
		JLabel label_1 = new JLabel("");
		panel.add(label_1, "2, 2, left, center");
		
		JLabel label_2 = new JLabel("");
		panel.add(label_2, "2, 2, left, center");
		
		JLabel label_3 = new JLabel("");
		panel.add(label_3, "2, 2, left, center");
		
		JLabel label_4 = new JLabel("");
		panel.add(label_4, "2, 2, left, center");
		
		radiobtns = new Vector<JRadioButton>();
		
		JRadioButton rdbtnOpcao1 = new JRadioButton("Opcao1");
		buttonGroup.add(rdbtnOpcao1);
		radiobtns.add(rdbtnOpcao1);
		rdbtnOpcao1.setBackground(Color.WHITE);
		panel.add(rdbtnOpcao1, "2, 4, fill, center");
		
		JRadioButton rdbtnOpcao2 = new JRadioButton("Opcao2");
		buttonGroup.add(rdbtnOpcao2);
		radiobtns.add(rdbtnOpcao2);
		rdbtnOpcao2.setBackground(Color.WHITE);
		panel.add(rdbtnOpcao2, "2, 6, fill, center");
		
		JRadioButton rdbtnOpcao3 = new JRadioButton("Opcao3");
		buttonGroup.add(rdbtnOpcao3);
		radiobtns.add(rdbtnOpcao3);
		rdbtnOpcao3.setBackground(Color.WHITE);
		panel.add(rdbtnOpcao3, "2, 8, fill, center");
		
		JRadioButton rdbtnOpcao4 = new JRadioButton("Opcao4");
		buttonGroup.add(rdbtnOpcao4);
		radiobtns.add(rdbtnOpcao4);
		rdbtnOpcao4.setBackground(Color.WHITE);
		panel.add(rdbtnOpcao4, "2, 10, fill, center");
		
		JRadioButton rdbtnOpcao5 = new JRadioButton("Opcao5");
		buttonGroup.add(rdbtnOpcao5);
		radiobtns.add(rdbtnOpcao5);
		rdbtnOpcao5.setBackground(Color.WHITE);
		panel.add(rdbtnOpcao5, "2, 12, fill, center");
		
		JRadioButton rdbtnOpcao6 = new JRadioButton("Opcao6");
		buttonGroup.add(rdbtnOpcao6);
		radiobtns.add(rdbtnOpcao6);
		rdbtnOpcao6.setBackground(Color.WHITE);
		panel.add(rdbtnOpcao6, "2, 14, fill, center");
		
		onChangeQuestion();
	}
	
	public void beforeChangeQuestion(){
		if(correctop <= 0) return;
		JRadioButton rightbtn = radiobtns.get(correctop-1);
		rightbtn.setForeground(Color.BLACK);
		buttonGroup.clearSelection();
	}
	
	public void onChangeQuestion(){
		if(this.tests == null){
			this.btnBack.setEnabled(false);
			this.btnGo.setEnabled(false);
		}else{
			this.btnBack.setEnabled((this.questindex > 0));
			this.btnGo.setEnabled((this.questindex < (this.questions.size() - 1)));
		}
		if(txtQuestao != null){
			txtQuestao.setCaretPosition(0); //Apenas JRE >= 6
			int choosedop = this.choosedsAnswers.get(questindex);
			if(choosedop < 6 & choosedop >= 0)
				this.radiobtns.get(choosedop).setSelected(true);
		}
	}
	
	public void setQuestionData(String source, String question, Vector<String> options, byte lcorrectop){
		beforeChangeQuestion();
		txtQuestao.setText("("+source+") "+question);
		int optsnum = options.size();
		for(byte i=0; i<radiobtns.size(); i++){
			if(i<optsnum){
				radiobtns.get(i).setText(optionsLabel[i] + options.elementAt(i));
				radiobtns.get(i).setVisible(true);
			}else{
				radiobtns.get(i).setText("");
				radiobtns.get(i).setVisible(false);
				
			}
		}
		this.correctop = lcorrectop;
		onChangeQuestion();
	}
	
	public void setQuestionData(Question q){
		if(q == null){
			Vector<String> opts = new Vector<String>();
			opts.add("Ok!");
			try{
				QuestionID actquest = questions.get(questindex);
				setQuestionData("Erro", "Questão corrompida! ID" + actquest.id + "TEST" + actquest.test, opts, (byte) 0);
			}catch(Exception e){
				setQuestionData("Erro", "Questão corrompida! Possível erro na data do aplicativo. Por favor, limpe-a.", opts, (byte) 0);
			}
		}else{
			beforeChangeQuestion();
			txtQuestao.setText(q.getProblemText());
			Vector<String> as = q.getAnswers();
			int optsnum = as.size();
			for(byte i=0; i<radiobtns.size(); i++){
				if(i<optsnum){
					String puretext = optionsLabel[i] + as.elementAt(i);
					if(puretext.length() < txtQuestao.getColumns())
						radiobtns.get(i).setText(puretext);
					else
						radiobtns.get(i).setText("<html><body height=\"32\" width="+(getSize().width - 60)+">"+puretext+"</body></html>");
					radiobtns.get(i).setVisible(true);
				}else{
					radiobtns.get(i).setText("");
					radiobtns.get(i).setVisible(false);
					
				}
			}
			this.correctop = q.getRightAnswerId();
			onChangeQuestion();
		}
	}
	
	public static void doTests(Vector<String> testsSNames){
		Vector<Test> ltests = new Vector<Test>();
		for(String testSName :testsSNames){
			try {
				Test test = new LighterTest(testSName);
				ltests.add(test);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		QuestionsRunner frame = new QuestionsRunner(ltests);
		Main.changeVisualTo(frame);
	}
	

}
