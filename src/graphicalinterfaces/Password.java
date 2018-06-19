package graphicalinterfaces;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Password extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3327260382151894871L;
	private JButton button = new JButton();
	private JButton cancelButton = new JButton();
	private JPasswordField passwordField = new JPasswordField();
	public String password;

	public Password() {
	
		this.button.setText("Confirm");
		this.cancelButton.setText("Cancel");
		this.passwordField.setText("");
		
		this.passwordField.setPreferredSize(new Dimension(300, 40));
		this.passwordField.setSize(300, 40);
		
		JLabel text = new JLabel();
		text.setText("Please enter the password here");

		BorderLayout layout = new BorderLayout();
		layout.setHgap(50);
		layout.setVgap(30);
		this.setLayout(layout);

		this.add(text, BorderLayout.PAGE_START);
		this.add(passwordField, BorderLayout.LINE_START);
		this.add(button, BorderLayout.CENTER);
		this.add(cancelButton, BorderLayout.LINE_END);
		
		button.addActionListener(this);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
			
		});
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		this.setVisible(true);
		this.setOpaque(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button){
			char[] passw = passwordField.getPassword();
			String w8woord = "";
			for(Character c : passw){
				w8woord = w8woord + c.toString();
			}
			this.password = w8woord;
			this.setVisible(false);
		}
	}
}
