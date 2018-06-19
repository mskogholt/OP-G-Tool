package tests;

import containers.Result;

public class SerializeTest {

	public static void main(String[] args) {
		
		String toParse = "http://www.tht.nl;tht;Restaurant;Amsterdam;phones:020 760 4820:020 760 4820:020 760 4820:020 760 4820 ;emails:info@tht.nl:info@tht.nl:info@tht.nl:reserveren@tht.nl;:info@tht.nl;adresses:IJpromenade 2# 1031 KT Amsterdam#";
		Result res = Result.parse(toParse);

		System.out.println(toParse);
		System.out.println(res);
	}

}
