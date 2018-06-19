package io;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import containers.Adress;
import containers.HashList;
import containers.Result;

public class Writer {

	public static void savePerQuery(HashList<Result> results, String folder) throws Exception {
		HashMap<String, HashList<Result>> queryMap = new HashMap<String, HashList<Result>>();
		for(int i=0; i<results.size(); i++) {
			Result result = results.get(i);
			String key = result.getWhat()+", " + result.getWhere();
			if(queryMap.containsKey(key)) {
				HashList<Result> temp = queryMap.get(key);
				temp.add(result);
				queryMap.put(key, temp);
			} else {
				HashList<Result> temp = new HashList<Result>();
				temp.add(result);
				queryMap.put(key, temp);
			}
		}

		for(String key : queryMap.keySet()) {
			saveResults(queryMap.get(key), key, folder);
		}
	}

	public static void saveResults(HashList<Result> results, String title, String folder) throws Exception {

		XSSFWorkbook wb = new XSSFWorkbook();
		String file_title = title;
		title = title.replace("/", "");
		title = title.replace("\\", "");
		title = title.replace("?", "");
		title = title.replace("*", "");
		title = title.replace("]", "");
		title = title.replace("[", "");
		title = title.replace(":", "");

		title = "Leads of " + title;
		if(title.length()>31){
			title = title.substring(0, 31);
		}
		XSSFSheet sheet = wb.createSheet(title);
		int counter = 0;
		for(int i=0; i<results.size(); i++){
			Result res = results.get(i);
			if(res.getNumbers().size()!=0){
				for(String number : res.getNumbers()){
					XSSFRow row = sheet.createRow(counter);

					XSSFCell cellZero = row.createCell(0);
					cellZero.setCellValue(res.getWhat());

					XSSFCell cellWhere = row.createCell(1);
					cellWhere.setCellValue(res.getWhere());

					XSSFCell cellOne = row.createCell(2);
					cellOne.setCellValue(res.getCompany());

					XSSFCell cellTwo = row.createCell(3);
					cellTwo.setCellValue(number);

					XSSFCell cellThree = row.createCell(4);
					XSSFCell cellFour = row.createCell(5);


					if(res.getAdresses().size()>0){
						Adress adres = res.getAdresses().get(0);
						cellThree.setCellValue(adres.getStreet());
						cellFour.setCellValue(adres.getPostal()+" "+adres.getCity());
					}

					int tempCounter = 6;
					for(String email : res.getEmails()){
						XSSFCell cell = row.createCell(tempCounter);
						cell.setCellValue(email);
						tempCounter++;
					}
					counter++;
				}
			}else{
				XSSFRow row = sheet.createRow(counter);

				XSSFCell cellZero = row.createCell(0);
				cellZero.setCellValue(res.getWhat());

				XSSFCell cellWhere = row.createCell(1);
				cellWhere.setCellValue(res.getWhere());

				XSSFCell cellOne = row.createCell(2);
				cellOne.setCellValue(res.getCompany());

				XSSFCell cellTwo = row.createCell(3);
				cellTwo.setCellValue("-");

				XSSFCell cellThree = row.createCell(4);
				XSSFCell cellFour = row.createCell(5);

				if(res.getAdresses().size()>0){
					Adress adres = res.getAdresses().get(0);
					cellThree.setCellValue(adres.getStreet());
					cellFour.setCellValue(adres.getPostal()+" "+adres.getCity());
				}

				int tempCounter = 6;
				for(String email : res.getEmails()){
					XSSFCell cell = row.createCell(tempCounter);
					cell.setCellValue(email);
					tempCounter++;
				}
				counter++;
			}
		}

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh.mm.ss.SSS");
		Date date = new Date();
		String currentDate = dateFormat.format(date);

		FileOutputStream fileOut = new FileOutputStream(folder+"/Leads "+file_title+" "+currentDate+".xlsx");
		wb.write(fileOut);
		fileOut.close();
		wb.close();
	}
}
