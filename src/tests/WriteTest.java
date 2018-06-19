package tests;

import containers.HashList;
import containers.Result;
import io.Reader;
import io.Writer;

public class WriteTest {

	public static void main(String[] args) throws Exception {
		String file_name = "Cache 11a35a68-2b87-4fba-af4d-6c1ddd52f42d.txt";
		String folder = "C:/Users/Martin/Documents/Programming/Workspace/Office Palace/";
		
		HashList<Result> results = Reader.readResults(folder+file_name);
		
		System.out.println(results.size());
		
//		Writer.saveResults(results, "TestI", folder);
		Writer.savePerQuery(results, folder);

	}

}
