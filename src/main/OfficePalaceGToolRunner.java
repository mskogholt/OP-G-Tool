/**
 * 
 */
package main;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import containers.HashList;
import containers.Query;
import containers.Result;
import graphicalinterfaces.InputPanel;
import graphicalinterfaces.MessagePanel;
import graphicalinterfaces.Password;
import io.Reader;
import io.Writer;
import managers.Manager;
import models.WebBrowser;

/**
 * @author martin.skogholt
 *
 */
public class OfficePalaceGToolRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

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

		while(true) {
			ImageIcon imageIcon = new ImageIcon("./Logo.png"); // load the image to a imageIcon
			Image image = imageIcon.getImage(); // transform it 
			Image logo = image.getScaledInstance(1000, 1000,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  

			JFrame frame = new JFrame("Lead Generator Office Palace");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setIconImage(logo);

			/**
			 * 
			 */
			InputPanel panel = new InputPanel();
			frame.add(panel);
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

			frame.setVisible(false);

			MessagePanel messenger = new MessagePanel();
			panel.setVisible(false);
			frame.add(messenger);
			frame.pack();
			frame.setVisible(true);

			messenger.setMessage("Loading resources...");

			WebBrowser web = new WebBrowser();
			try {
				HtmlPage passwordPage = web.getPage("https://mskogholtwordpresscom.wordpress.com/");
				if(passwordPage.asText().contains("Password should be asked: ON")){
					messenger.setVisible(false);
					Password passWord = new Password();
					frame.setContentPane(passWord);
					frame.pack();

					while(passWord.password==null){
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					passWord.setVisible(false);

					if(!passWord.password.equals("@Ihlen70a")){
						throw new IllegalArgumentException("Wrong password!");
					}
				}	
			} catch (Exception e) {
				messenger.setVisible(true);
				messenger.setMessage("Fatal error, contact admin");
			} finally {
				web.close();
				messenger.setVisible(true);
			}

			ExecutorService executor = Executors.newSingleThreadExecutor();

			ArrayList<Query> queries = new ArrayList<Query>();
			if(panel.isSingleQuery()) {
				queries.add(panel.getQuery());
			}else {
				try {
					queries.addAll(Reader.readQueries(panel.getReadLocation()));
				} catch (Exception e){
					JOptionPane.showMessageDialog(null, "Error Reading Queries: \n" + e.toString(), "Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
			String id = UUID.randomUUID().toString();

			BufferedWriter writer = null;
			try {
				File file = new File("./Cache "+id+".txt");

				// if file doesn't exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error Creating Cache Writer: \n" + e.toString(), "Error!", JOptionPane.ERROR_MESSAGE);
			}

			try {
				Manager manager = new Manager(panel, messenger, queries, id, writer);

				Future<Boolean> future = executor.submit(manager);

				while(!future.isDone()) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(messenger.isCancelled()) {
						try {
							writer.flush();
							writer.close();

							messenger.setMessage("Run has been cancelled. Trying to retrieve from cache");

							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}

							try {
								HashList<Result> results = Reader.readResults("./Cache "+id+".txt");
								messenger.setMessage("Retrieved " + results.size() + " from cache!");
								if(panel.isSeparateFiles()) {
									Writer.savePerQuery(results, panel.getSaveLocation());
								} else {
									Writer.saveResults(results, "", panel.getSaveLocation());
								}
								JOptionPane.showMessageDialog(null, "Succesfully cancelled. Results can be found in " + panel.getSaveLocation(), "Done!", JOptionPane.PLAIN_MESSAGE);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null, "Error reading cache: \n" + e.toString(), "Error!", JOptionPane.ERROR_MESSAGE);
							}
						} catch (IOException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Error Cancelling: \n" + e.toString(), "Error!", JOptionPane.ERROR_MESSAGE);
						}
						System.exit(1);
					}
				}

				messenger.setMessage("Collection done!");

				boolean done = false;
				try {
					done = future.get();
				} catch (Exception e) {
					try {
						writer.flush();
						writer.close();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error flushing cache writer: \n" + e.toString(), "Error!", JOptionPane.ERROR_MESSAGE);
					}
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error in manager: \n" + e.toString(), "Error!", JOptionPane.ERROR_MESSAGE);
				}

				if(!done) {
					messenger.setMessage("Something went wrong! Trying to retrieve from cache");

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					HashList<Result> results = Reader.readResults("./Cache "+id+".txt");
					messenger.setMessage("Retrieved " + results.size() + " from cache!");
					if(panel.isSeparateFiles()) {
						Writer.savePerQuery(results, panel.getSaveLocation());
					} else {
						Writer.saveResults(results, "", panel.getSaveLocation());
					}
				}
				messenger.setMessage("Trying to shutdown...");
				executor.shutdown();
				while(!executor.isTerminated()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				JOptionPane.showMessageDialog(null, "All done! Results can be found in " + panel.getSaveLocation(), "Done!", JOptionPane.PLAIN_MESSAGE);
				messenger.setMessage("All done! Results can be found in " + panel.getSaveLocation());
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Fatal Error! Try again or contact admin" + "\n" + e, "Error!", JOptionPane.ERROR_MESSAGE);
			}
			messenger.setVisible(false);
			frame.setVisible(false);
		}
	}
}
