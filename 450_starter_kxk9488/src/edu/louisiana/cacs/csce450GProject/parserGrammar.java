package edu.louisiana.cacs.csce450GProject;

import java.util.HashMap;
import java.util.Map;

public class parserGrammar {
	Map<String, String> Grammar = new HashMap<String, String>();
	public void createGrammarTable()
	{
		
		Grammar.put("1" ,"E->E+T" );
		Grammar.put("2" ,"E->T" );
		Grammar.put("3" ,"T->T*F" );
		Grammar.put("4" ,"T->F" );
		Grammar.put("5" ,"F->(E)" );
		Grammar.put("6" ,"F->id" );
			
	}
}
