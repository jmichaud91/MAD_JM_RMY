package form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

public class mainForm extends JFrame {
	private JPanel mainPanel;
	private JLabel learningFile;
	private JLabel validationFile;
	private JTextArea learningFilePath;
	private JTextArea validationFilePath;
	private JButton browseLearningFile; 
	private JButton startLearningButton;
	private JButton browseValidationFile;
	private JButton startValidationButton;
	private JTextArea traceTextArea;
	
	public mainForm()
	{
		initComponents();
	}
	
	private void setUpValidationArea(final JPanel mainPanel, GridBagConstraints constraint)
	{
		validationFile = new JLabel("Choisir fichier de validation : ");
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = 2;
		constraint.insets = new Insets(0,10,0,0);
		mainPanel.add(validationFile, constraint);

		validationFilePath = new JTextArea();
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.weightx = 0.85;
		constraint.gridx = 1;
		constraint.gridy = 2;
		//c.insets = new Insets(top, left, bottom, right)
		constraint.insets = new Insets(0,20,0,10);
		mainPanel.add(validationFilePath, constraint);
		
		browseValidationFile = new JButton("...");
		browseValidationFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Fichier de validation", "csv", "csv");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(mainPanel);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	validationFilePath.setText(chooser.getSelectedFile().getAbsolutePath());
			    }
			}
		});
		
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.weightx = 0.1;
		constraint.gridx = 2;
		constraint.gridy = 2;
		constraint.insets = new Insets(0,0,0,10);
		mainPanel.add(browseValidationFile, constraint);
	}
	
	private void setUpLearningArea(final JPanel mainPanel, GridBagConstraints constraint)
	{
		learningFile = new JLabel("Choisir fichier d'apprentissage : ");
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.insets = new Insets(10,10,0,0);
		mainPanel.add(learningFile, constraint);
		
		learningFilePath = new JTextArea();
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.weightx = 0.85;
		constraint.gridx = 1;
		constraint.gridy = 0;
		//c.insets = new Insets(top, left, bottom, right)
		constraint.insets = new Insets(10,20,0,10);
		mainPanel.add(learningFilePath, constraint);
		
		browseLearningFile = new JButton("...");
		browseLearningFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Fichier d'apprentissage", "csv", "csv");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(mainPanel);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	learningFilePath.setText(chooser.getSelectedFile().getAbsolutePath());
			    }
			}
		});
		
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.weightx = 0.1;
		constraint.gridx = 2;
		constraint.gridy = 0;
		constraint.insets = new Insets(10,0,0,10);
		mainPanel.add(browseLearningFile, constraint);
	}

	private void setUpStartButtons(final JPanel mainPanel, GridBagConstraints constraint)
	{
		startLearningButton = new JButton("Commencer apprentissage !");
		startLearningButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = 1;
		constraint.weightx = 0.9;
		constraint.gridwidth = 3;
		constraint.insets = new Insets(10,10,10,10);
		mainPanel.add(startLearningButton, constraint);
		

		startValidationButton = new JButton("Commencer la validation !");
		startValidationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = 3;
		constraint.weightx = 0.9;
		constraint.gridwidth = 3;
		constraint.insets = new Insets(10,10,10,10);
		mainPanel.add(startValidationButton, constraint);
	}
	
	private void initComponents(){
		this.setSize(800, 350);
		this.setTitle("LOG635 - TP3 - Équipe H");
		mainPanel = new JPanel(new SpringLayout());
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		setUpLearningArea(mainPanel, c);

		setUpValidationArea(mainPanel, c);
		
		setUpStartButtons(mainPanel, c);
		
		traceTextArea = new JTextArea();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 0.9;
		c.ipady = 100;
		c.gridwidth = 3;
		mainPanel.add(traceTextArea, c);
		
		this.add(mainPanel);
	}
}

