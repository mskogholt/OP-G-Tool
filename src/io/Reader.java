/**
 * 
 */
package io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import containers.HashList;
import containers.Query;
import containers.Result;

/**
 * @author martin.skogholt
 *
 */
public class Reader {

	public static HashList<Result> readResults(String file) throws Exception {
		HashList<Result> results = new HashList<Result>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		while(line!=null) {
			Result result = Result.parse(line);
			results.add(result);
			line =reader.readLine();
		}
		reader.close();
		if(results.size()==0){
			throw new Exception("No cached results were found! \n"+
		"This could be due to the fact that no results were collected before"
		+ "before the run was cancelled");
		}
		return results;
	}

	public static ArrayList<Query> readQueries(String fileLocation) throws IOException{
		ArrayList<Query> queries = new ArrayList<Query>();

		InputStream input = new FileInputStream(fileLocation);
		XSSFWorkbook workBook = new XSSFWorkbook(input);
		try {
			XSSFSheet sheet = workBook.getSheetAt(0);
			Iterator<Row> queriesRowIt = sheet.rowIterator();
			queriesRowIt.next();
			while(queriesRowIt.hasNext()){
				XSSFRow row = (XSSFRow) queriesRowIt.next();
				XSSFCell cell = row.getCell(0);

				Query query = new Query(cell.getStringCellValue());

				if(row.getLastCellNum()>1){
					XSSFCell cellTwo = row.getCell(1);
					query.setWhere(cellTwo.getStringCellValue());
				}
				queries.add(query);
			}
		} finally {
			workBook.close();
		}
		return queries;
	}
}
