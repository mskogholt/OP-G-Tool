package tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import containers.Adress;
import containers.Result;

public class Dummy {



	public static void main(String[] args) throws Exception{

//		System.out.println(Reader.readResults("./tempresults of run "+1+".txt").size());
//		System.exit(1);
		
		Result res = new Result("www.testints.nl");
		res.setCompany("Martins Test Bedrijf");
		res.setWhat("Test");
		res.setWhere("Test");

		res.addEmail("test@test.com");
		res.addEmail("test@test.nl");

		res.addNumber("06test01");
		res.addNumber("06test02");
		res.addNumber("06test03");

		Adress ad1 = new Adress("testweg 1","3035 Test","Teststad");
		Adress ad2 = new Adress("testweg 2","3035 Test","Teststad");
		Adress ad3 = new Adress("testweg 3","3035 Test","Teststad");

		res.addAdress(ad1);
		res.addAdress(ad2);
		res.addAdress(ad3);

		BufferedWriter writer = null;
		try {
			File file = new File("./tempresults of run "+1+".txt");

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		for(int i=0; i<100; i++) {
			if(writer!=null) {
				res.setUrl("www.test"+i+".nl");
				writer.write(res.serialize());
				writer.newLine();
			}
		}

		if(writer!=null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//		WebBrowser web = new WebBrowser();
		//		String link = "https://www.proxfree.com/";
		//		DomElement goButton = null;
		//		String site = "http://www.google.nl";
		//		if(link.contains("proxfree")){
		//			Random random = new Random();
		//			// Proxfree
		//			// Get the first page
		//			HtmlPage page = web.getPage("https://www.proxfree.com/");
		//
		//			HtmlSelect server = page.getHtmlElementById("pfserverDropdown");
		//			int max = server.getOptionSize()-1;
		//			int min = 0;
		//			int selectedIndex = random.nextInt(max - min + 1) + min;
		//			page = server.setSelectedAttribute(server.getOption(selectedIndex), true);
		//
		//			HtmlSelect select = page.getElementByName("pfipDropdown");
		//			max = select.getOptionSize()-1;
		//			min = 0;
		//			selectedIndex = random.nextInt(max - min + 1) + min;
		//			page = select.setSelectedAttribute(select.getOption(selectedIndex), true);
		//
		//			HtmlInput input = page.getElementByName("get");
		//			input.setValueAttribute(site);
		//			goButton = page.getFirstByXPath("/html/body/div[2]/div/form/input[2]");
		//		}
		//
		//
		//		HtmlPage page = goButton.click();
		//		// Gets the google search bar as htmlinput and inputs the query to be googled.
		//		HtmlInput input = page.getElementByName("q");
		//
		//		input.setValueAttribute("Bakker");
		//
		//		HtmlSubmitInput submit = page.getElementByName("btnK");
		//
		//		HtmlPage googlePage = submit.click();
		//		System.out.println(googlePage.asText());
		//		System.out.println("--------------------------------------------------------------------------------------");
		//		System.out.println(googlePage.asXml());
		//
		//		web.close();
	}

}
