package graphicalinterfaces;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;

import containers.Query;
import models.TerminationLimit;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;

public class InputPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8213411439902387817L;
	private JTextField terminationLimitInput;
	private JTextField saveToField;
	private JTextField whatField;
	private JTextField whereField;
	private JComboBox<String> crawlerSelect;
	private JComboBox<String> terminationTypeSelect;
	private JComboBox<String> queryTypeSelect;
	private boolean completed = false;
	private JCheckBox singleFile;
	private JTextField threads;

	public static void main(String[] args) {
		UIManager.put("control", new Color(255,255,255));
		UIManager.put("nimbusOrange", new Color(51,98,140));

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {}

		ImageIcon imageIcon = new ImageIcon("./Logo.png"); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image logo = image.getScaledInstance(1000, 1000,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  

		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(logo);

		InputPanel panel = new InputPanel();
		frame.getContentPane().add(panel);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		while(!panel.isCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Go!");
	}

	/**
	 * Create the panel.
	 */
	public InputPanel() {
		setLayout(new MigLayout("", "[grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink]", "[grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink][grow,shrink]"));

		int strutWidth = 60;
		int strutMargin = 10;
		Component marginStrut = Box.createVerticalStrut(strutMargin);
		add(marginStrut, "cell 1 0");

		Component marginStrut2 = Box.createHorizontalStrut(strutMargin);
		add(marginStrut2, "cell 0 1");

		JLabel crawlerLabel = new JLabel("Select website to crawl");
		add(crawlerLabel, "cell 1 1");

		JLabel terminationTypeLabel = new JLabel("Select termination type");
		add(terminationTypeLabel, "cell 3 1");

		JLabel terminationLimitLabel = new JLabel("Enter termination limit");
		add(terminationLimitLabel, "cell 5 1");

		String[] crawlOptions = { 
				"http://www.google.nl", 
				"http://www.google.be", 
				"http://www.google.de", 
				"http://www.google.it", 
				"http://www.google.es", 
				"http://www.google.co.uk", 
				"http://www.google.com", 
				"http://www.google.fr",
				"http://www.test.nl"
		};

		crawlerSelect = new JComboBox<String>();
		for(String item : crawlOptions) {
			crawlerSelect.addItem(item);
		}
		add(crawlerSelect, "cell 1 2,growx,alignx left,aligny top");

		terminationTypeSelect = new JComboBox<String>();
		terminationTypeSelect.addItem("Page Limit");
		terminationTypeSelect.addItem("Time Limit");
		add(terminationTypeSelect, "cell 3 2,growx,alignx left,aligny top");

		Component strutOne = Box.createHorizontalStrut(strutWidth);
		add(strutOne, "cell 2 2");

		Component strutTwo = Box.createHorizontalStrut(strutWidth);
		add(strutTwo, "cell 4 2");

		terminationLimitInput = new JTextField();
		add(terminationLimitInput, "cell 5 2,growx");
		terminationLimitInput.setColumns(10);

		Component marginStrut3 = Box.createHorizontalStrut(strutMargin);
		add(marginStrut3, "cell 6 2");

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		add(verticalStrut_1, "cell 1 3");

		JLabel saveToLabel = new JLabel("Save results to");
		add(saveToLabel, "cell 1 4");

		JLabel lblOfThreads = new JLabel("# of Threads");
		add(lblOfThreads, "cell 3 4");

		saveToField = new JTextField();
		add(saveToField, "cell 1 5,growx");
		saveToField.setColumns(10);

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JButton saveButton = new JButton("...");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Handle open button action.
				if (e.getSource() == saveButton) {
					int returnVal = fc.showOpenDialog(InputPanel.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						saveToField.setText(file.getAbsolutePath());
					} 
				}
			}
		});		
		add(saveButton, "cell 2 5");

		singleFile = new JCheckBox("Separate Excel files per Query?");
		singleFile.setVisible(false);

		threads = new JTextField();
		threads.setColumns(10);
		add(threads, "cell 3 5,growx");
		add(singleFile, "cell 5 5");

		Component verticalStrut_2 = Box.createVerticalStrut(20);
		add(verticalStrut_2, "cell 1 6");

		JLabel queryTypeLabel = new JLabel("Select...");
		add(queryTypeLabel, "cell 1 7");

		JLabel whatLabel = new JLabel("What?");
		add(whatLabel, "cell 3 7");

		JLabel whereLabel = new JLabel("Where?");
		add(whereLabel, "cell 5 7");

		queryTypeSelect = new JComboBox<String>();
		queryTypeSelect.addItem("Enter manually");
		queryTypeSelect.addItem("Read from file");
		add(queryTypeSelect, "cell 1 8,growx");


		whatField = new JTextField();
		add(whatField, "cell 3 8,growx");
		whatField.setColumns(10);

		whereField = new JTextField();
		add(whereField, "cell 5 8,growx");
		whereField.setColumns(10);

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files","xlsx"));

		JButton readFromButton = new JButton("...");
		readFromButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Handle open button action.
				if (e.getSource() == readFromButton) {
					int returnVal = fileChooser.showOpenDialog(InputPanel.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						whatField.setText(file.getAbsolutePath());
					} 
				}
			}
		});
		add(readFromButton, "cell 4 8");
		readFromButton.setVisible(false);

		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut, "cell 5 9");

		JButton runButton = new JButton("Run!");
		add(runButton, "cell 5 10,alignx right");

		queryTypeSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(queryTypeSelect.getSelectedIndex()==1) {
					whereLabel.setVisible(false);
					whereField.setVisible(false);

					whatLabel.setText("Read queries from");
					readFromButton.setVisible(true);
					singleFile.setVisible(true);

				} else {
					readFromButton.setVisible(false);
					singleFile.setVisible(false);
					whatLabel.setText("What?");
					whereLabel.setVisible(true);
					whereField.setVisible(true);
				}

			}
		});

		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validateInput()) {
					completed = true;
				}
			}
		});
	}

	public boolean validateInput() {
		boolean allValid = true;
		try {
			if(terminationLimitInput.getText().length()<1) {
				throw new NullPointerException("Termination Input Empty");
			}
			Integer.parseInt(terminationLimitInput.getText());
		} catch (Exception e) {
			allValid = false;
			JOptionPane.showMessageDialog(null, "Invalid Input"+"\n"+"Termination limit is either empty or not a whole number", "Error!", JOptionPane.ERROR_MESSAGE);
		}

		try {
			File file = new File(saveToField.getText());
			if(!file.exists() || !file.isDirectory()) {
				throw new Exception("Unvalid Directory selected");
			}
		} catch (Exception e) {
			allValid = false;
			JOptionPane.showMessageDialog(null, "Invalid Input"+"\n"+"Directory to save results is not correct", "Error!", JOptionPane.ERROR_MESSAGE);
		}

		if(queryTypeSelect.getSelectedIndex()==1) {
			try {
				File file = new File(whatField.getText());
				if(!file.exists() || !file.getAbsolutePath().endsWith(".xlsx")) {
					throw new Exception("Unvalid file selected");
				}
			} catch (Exception e) {
				allValid = false;
				JOptionPane.showMessageDialog(null, "Invalid Input"+"\n"+"File to read queries from is incorrect", "Error!", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			if(whatField.getText().length()<1) {
				allValid = false;
				JOptionPane.showMessageDialog(null, "Invalid Input"+"\n"+"\"What?\" field is empty", "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}

		if(!allValid) {
			System.out.println("Input not valid");
		}
		return allValid;
	}

	public boolean isSingleQuery() {
		return queryTypeSelect.getSelectedIndex()==0;
	}

	public String getSite() {
		return (String) this.crawlerSelect.getSelectedItem();
	}

	public TerminationLimit getTerminationLimit() {
		return new TerminationLimit((String) this.terminationTypeSelect.getSelectedItem(), Integer.parseInt(this.terminationLimitInput.getText()));
	}

	public String getSaveLocation() {
		return this.saveToField.getText();
	}

	public boolean isSeparateFiles() {
		return this.singleFile.isSelected();
	}

	public String getReadLocation() {
		return this.whatField.getText();
	}

	public Query getQuery() {
		return new Query(whatField.getText(), whereField.getText());
	}

	public int getThreads(){
		int threads = 2;
		if(this.threads.getText().length()>0){
			try {
				threads = Integer.parseInt(this.threads.getText());
			} catch (Exception e){}
		}
		return threads;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

}
