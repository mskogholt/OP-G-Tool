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
import java.awt.Font;

import javax.swing.JProgressBar;
import java.awt.Component;
import javax.swing.Box;

public class MessagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9159171026501140257L;
	private JLabel message;
	private JLabel status;
	private JProgressBar progressBar;
	private boolean cancelled = false;
	private Component horizontalStrut;
	private Component verticalStrut;
	
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
		setLayout(new MigLayout("fillx", "[][grow]", "[][][][][grow][]"));

		Font font = new Font("Sylfaen",Font.PLAIN, 18);
		
		verticalStrut = Box.createVerticalStrut(40);
		add(verticalStrut, "cell 1 0");

		status = new JLabel("Initializing.....");
		status.setFont(font);
		add(status, "cell 1 1,alignx left");
		
		horizontalStrut = Box.createHorizontalStrut(40);
		add(horizontalStrut, "cell 0 2");

		message = new JLabel("1 2 3 ...");
		message.setFont(font);
		add(message, "cell 1 3,wmin 600,alignx left");

		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setFont(font);
		add(progressBar, "cell 1 2,alignx left");

		JButton cancel = new JButton("Cancel...");
		cancel.setFont(font);
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancelled = true;
			}

		});
		add(cancel, "cell 1 5,alignx right");
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
				try {
					((Window) this.getTopLevelAncestor()).pack();
				} catch (Exception e) {}
			}
			if(prog>=0 && prog<=100) {
				progressBar.setValue(prog);
			}
		}
	}

	public void setStatus(String message) {
		synchronized(status) {
			this.status.setText(message);
		}
	}

	public void setMessage(String message) {
		synchronized(message) {
			this.message.setText(message);
		}
	}
}
