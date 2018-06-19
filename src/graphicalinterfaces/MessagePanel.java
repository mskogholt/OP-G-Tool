package graphicalinterfaces;

import java.awt.Color;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JProgressBar;

public class MessagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9159171026501140257L;
	private JLabel message;
	private JLabel status;
	private JProgressBar progressBar;
	private boolean cancelled = false;
	
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

		MessagePanel panel = new MessagePanel();
		frame.getContentPane().add(panel);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		for(int i=0; i<10; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int prog = i+1;
			panel.setMessage("Done with query " + prog + " out of 10");
		}
	}

	/**
	 * Create the panel.
	 */
	public MessagePanel() {
		setLayout(new MigLayout("", "[][][]", "[][][][][][][]"));

		int strutSize = 60;
		Component verticalStrut = Box.createVerticalStrut(strutSize);
		add(verticalStrut, "cell 1 0,aligny top");

		status = new JLabel("");
		add(status, "cell 1 1");

		Component horizontalStrut = Box.createHorizontalStrut(strutSize);
		add(horizontalStrut, "cell 0 2,alignx left");

		message = new JLabel("");
		add(message, "cell 1 2,growx,alignx left,aligny top");

		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		add(progressBar, "cell 1 3");

		Component verticalStrut_2 = Box.createVerticalStrut(20);
		add(verticalStrut_2, "cell 1 4");

		JButton cancel = new JButton("Cancel...");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancelled = true;
			}

		});
		add(cancel, "cell 1 5,growx");

		Component horizontalStrut_1 = Box.createHorizontalStrut(strutSize);
		add(horizontalStrut_1, "cell 2 5,alignx right");

		Component verticalStrut_1 = Box.createVerticalStrut(strutSize);
		add(verticalStrut_1, "cell 1 6,aligny bottom");

	}

	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setProgress(int prog) {
		synchronized(progressBar) {
			if(progressBar.isIndeterminate()) {
				progressBar.setIndeterminate(false);
				progressBar.setMaximum(100);
				progressBar.setStringPainted(true);
			}
			if(prog>=0 && prog<=100) {
				progressBar.setValue(prog);
			}
		}
	}

	public void setStatus(String message) {
		synchronized(status) {
			this.status.setText(message);
			try {
				((Window) this.getTopLevelAncestor()).pack();
			} catch (Exception e) {}
		}
	}

	public void setMessage(String message) {
		synchronized(message) {
			this.message.setText(message);
			try {
				((Window) this.getTopLevelAncestor()).pack();
			} catch (Exception e) {}
		}
	}
}
