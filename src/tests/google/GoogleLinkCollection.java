package tests.google;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import containers.Query;
import containers.concurrency.RandomGenerator;
import graphicalinterfaces.MessagePanel;
import models.TerminationLimit;
import workers.links.LinkWorkerGoogle;

public class GoogleLinkCollection {

	public static void main(String[] args) throws Exception {
	

		ArrayList<String> proxies = new ArrayList<String>();
		for(int l=0; l<10; l++){
			proxies.add("http://fishproxy.com/"); // One
		}	
		for(int l=0; l<10; l++){
			proxies.add("http://xitenow.com/"); // One
		}
		for(int l=0; l<10; l++){
			proxies.add("http://webanonymizer.org/");
		}
		for(int k=0; k<10; k++){
			proxies.add("https://www.proxfree.com/");
		}
		for(int i=0; i<10; i++){
			proxies.add("https://www.filterbypass.me/"); // 1
		}
		for(int i=0; i<10; i++){
			proxies.add("http://www.unblockmyweb.com/"); // 1
		}
		for(int i=0; i<25; i++){
			proxies.add("https://www.proxfree.com/"); // 1
		}
		RandomGenerator gen = new RandomGenerator(proxies.size());

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

		ImageIcon imageIcon = new ImageIcon("./Logo.png"); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image logo = image.getScaledInstance(1000, 1000,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  

		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(logo);

		/**
		 * 
		 */
		MessagePanel panel = new MessagePanel();
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		Query queryBoth = new Query("Bakker","Rotterdam");
		String site = "http://www.google.nl/";
		TerminationLimit termination = new TerminationLimit("Page Limit",2);

		LinkWorkerGoogle worker = new LinkWorkerGoogle(queryBoth, site, proxies, gen, termination, panel);

		ArrayList<String> links = worker.call();

		for(String link : links) {
			System.out.println(link);
		}
		

	}

}
